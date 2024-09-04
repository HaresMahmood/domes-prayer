package com.example.domes.presentation.routes.settings.routes.notifications.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.Variant
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.example.domes.presentation.routes.settings.routes.notifications.routes.composables.ManagePrayerCard
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.IconSource

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class
)
@Composable
fun ManagePrayerScreen(
    navController: NavController? = null,
    prayer: Prayer,
    viewModel: ManagePrayerViewModel = hiltViewModel(),
) {
    viewModel.onEvent(ManagePrayerEvent.FetchPrayerData(prayer))

    val state = viewModel.state.value

    val notificationPermissionState =
        rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = {
                viewModel.onEvent(ManagePrayerEvent.ChangeNotificationsAllowed(true))
            }
        )

    val isNotificationsAllowed =
        notificationPermissionState.status.isGranted && state.notification?.isNotificationAllowed == true

    Scaffold(
        floatingActionButton = {
            if (isNotificationsAllowed) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(ManagePrayerEvent.AddNotification)
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                    )
                ) {
                    Icon(Icons.Filled.AddAlert, "Add")
                }
            }
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
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    FixedCard(
                        if (isNotificationsAllowed) Variant.VISUAL else Variant.FADED
                    ) {
                        SettingsListItem(
                            title = "Allow notifications",
                            icon = IconSource(Icons.Outlined.Notifications),
                            trailingContent = {
                                Switch(
                                    checked = isNotificationsAllowed,
                                    onCheckedChange = {
                                        if (notificationPermissionState.status.isGranted) {
                                            viewModel.onEvent(ManagePrayerEvent.ChangeNotificationsAllowed())
                                        } else {
                                            notificationPermissionState.launchPermissionRequest()
                                        }
                                    }
                                )
                            },
                            isLastItem = true,
                            onClick = {
                                if (notificationPermissionState.status.isGranted) {
                                    viewModel.onEvent(ManagePrayerEvent.ChangeNotificationsAllowed())
                                } else {
                                    notificationPermissionState.launchPermissionRequest()
                                }
                            }
                        )
                    }
                }
                state.notification?.notifications?.let { notifications ->
                    if (notifications.size > 0) {
                        item {
                            Column {
                                notifications.forEachIndexed { index, notification ->
                                    if (index == 0 || index == 1) {
                                        Text(
                                            if (index == 0) "Default" else "Additional",
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.padding(
                                                top = if (index == 0) 0.dp else 20.dp,
                                                bottom = 20.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                            )
                                        )
                                    }
                                    ManagePrayerCard(
                                        time = state.time,
                                        notification = notification,
                                        isEnabled = isNotificationsAllowed,
                                        isExpanded = state.expandedNotificationIndex == index,
                                        isFirstItem = index == 0 || index == 1,
                                        isDefaultNotification = index == 0,
                                        isLastItem = index == 0 || index == notifications.lastIndex,
                                        isBeforeExpandedItem = index == state.expandedNotificationIndex?.let { item -> item - 1 },
                                        isAfterExpandedItem = index == state.expandedNotificationIndex?.let { item -> item + 1 },
                                        changeOffset = { increment ->
                                            viewModel.onEvent(
                                                ManagePrayerEvent.ChangeOffset(
                                                    notification,
                                                    increment
                                                )
                                            )
                                        },
                                        changeOffsetRelation = {
                                            viewModel.onEvent(
                                                ManagePrayerEvent.ToggleOffsetPolarity(
                                                    notification
                                                )
                                            )
                                        },
                                        expand = { expanded ->
                                            viewModel.onEvent(
                                                ManagePrayerEvent.ChangeExpandedNotification(
                                                    if (expanded) null else index
                                                )
                                            )
                                        },
                                        remove = {
                                            viewModel.onEvent(
                                                ManagePrayerEvent.RemoveNotification(notification)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}