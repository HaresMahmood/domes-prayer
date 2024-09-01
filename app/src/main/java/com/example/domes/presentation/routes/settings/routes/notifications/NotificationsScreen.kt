package com.example.domes.presentation.routes.settings.routes.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.Variant
import com.example.domes.presentation.common.composables.card.concretions.NoticeCard
import com.example.domes.presentation.common.composables.listitem.IndicativeTrailingContent
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.IconSource

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class
)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel,
) {
    val state = viewModel.state.value

    val notificationPermissionState =
        rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = {
                viewModel.onEvent(NotificationsEvent.ChangeNotificationsAllowed(true))
            }
        )
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val isNotificationsAllowed =
        notificationPermissionState.status.isGranted && state.isNotificationsAllowed

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    FixedCard(
                        if (isNotificationsAllowed) Variant.VISUAL else Variant.FADED
                    ) {
                        SettingsListItem(
                            "Allow notifications",
                            IconSource(Icons.Outlined.Notifications),
                            trailingContent = {
                                Switch(
                                    checked = isNotificationsAllowed,
                                    onCheckedChange = {
                                        if (notificationPermissionState.status.isGranted) {
                                            viewModel.onEvent(NotificationsEvent.ChangeNotificationsAllowed())
                                        } else {
                                            notificationPermissionState.launchPermissionRequest()
                                        }
                                    }
                                )
                            },
                            isLastItem = true,
                            onClick = {
                                if (notificationPermissionState.status.isGranted) {
                                    viewModel.onEvent(NotificationsEvent.ChangeNotificationsAllowed())
                                } else {
                                    notificationPermissionState.launchPermissionRequest()
                                }
                            },
                        )
                    }
                }
                if (!notificationPermissionState.status.isGranted) {
                    item {
                        NoticeCard(
                            "Notifications are disabled",
                            IconSource(Icons.Outlined.NotificationsOff),
                            "Press the button above to enable them, or go to settings and enable them manually",
                        )
                    }
                }
                item {
                    FixedCard(
                        if (isNotificationsAllowed) Variant.DEFAULT else Variant.FADED
                    ) {
                        Prayer.ALL.forEachIndexed { index, prayer ->
                            val notification =
                                state.prayerNotifications.firstOrNull { it.prayer == prayer }
                            val notificationText = {
                                val notifications = notification?.notifications?.size ?: 1
                                val pluralText = if (notifications == 1) "" else "s"

                                "$notifications notification$pluralText"
                            }

                            SettingsListItem(
                                prayer.name,
                                IconSource(painterResource(prayer.iconResId)),
                                description = if (notification?.isNotificationAllowed === false) "Disabled" else null,
                                value = notificationText(),
                                trailingContent = {
                                    IndicativeTrailingContent {
                                        Switch(
                                            checked = notification?.isNotificationAllowed != false,
                                            onCheckedChange = {
                                                viewModel.onEvent(
                                                    NotificationsEvent.ChangePrayerNotification(
                                                        prayer
                                                    )
                                                )
                                            }
                                        )
                                    }
                                },
                                isEnabled = isNotificationsAllowed,
                                isLastItem = index == Prayer.ALL.lastIndex,
                                onClick = {
                                    navController.navigate("${Screen.ManagePrayer.route}?prayer=${prayer.name}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.Q)
//@Preview(
//    showBackground = true,
//    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
//)
//@Composable
//fun DomesPreview() {
//    DomesTheme {
//        NotificationsScreen()
//    }
//}