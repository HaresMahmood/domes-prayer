package com.example.domes.presentation.routes.settings.routes.notifications

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.Variant
import com.example.domes.presentation.common.composables.listitem.IndicativeTrailingContent
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.maxkeppeker.sheets.core.models.base.IconSource

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel
) {
    val state = viewModel.state.value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Notifications",
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },

        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    FixedCard(
                        if (state.isNotificationsAllowed) Variant.VISUAL else Variant.FADED
                    ) {
                        SettingsListItem(
                            "Allow notifications",
                            IconSource(Icons.Outlined.Notifications),
                            trailingContent = {
                                Switch(
                                    checked = state.isNotificationsAllowed,
                                    onCheckedChange = {
                                        viewModel.onEvent(NotificationsEvent.ChangeNotificationsAllowed())
                                    }
                                )
                            },
                            isLastItem = true,
                            onClick = {
                                viewModel.onEvent(NotificationsEvent.ChangeNotificationsAllowed())
                            },
                        )
                    }
                }
                item {
                    FixedCard(
                        if (state.isNotificationsAllowed) Variant.DEFAULT else Variant.FADED
                    ) {
                        Prayer.ALL.forEachIndexed { index, prayer ->
                            val notification = state.prayerNotifications.firstOrNull { it.prayer == prayer }
                            val notificationText = if (notification?.isNotificationAllowed != false) {
                                val notifications = notification?.notifications?.size ?: 1
                                val pluralText = if (notifications == 1) "" else "s"

                                "$notifications notification$pluralText"
                            }
                            else null

                            SettingsListItem(
                                prayer.name,
                                IconSource(painterResource(prayer.iconResId)),
                                description = notificationText.isNullOrBlank().let { "Disabled" },
                                value = notificationText,
                                trailingContent = {
                                    IndicativeTrailingContent() {
                                        Switch(
                                            checked = notification?.isNotificationAllowed != false,
                                            onCheckedChange = {
                                                viewModel.onEvent(NotificationsEvent.ChangePrayerNotification(prayer))
                                            }
                                        )
                                    }
                                },
                                isEnabled = state.isNotificationsAllowed,
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