package com.example.domes.presentation.routes.settings.routes.notifications.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.Variant
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.example.domes.presentation.routes.settings.routes.notifications.routes.composables.ManagePrayerCard
import com.maxkeppeker.sheets.core.models.base.IconSource

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ManagePrayerScreen(
    navController: NavController,
    prayer: Prayer,
    viewModel: ManagePrayerViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                prayer.name,
                backgroundText = prayer.arabicName,
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
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
                        if (state.notification?.isNotificationAllowed == true) Variant.VISUAL else Variant.FADED
                    ) {
                        SettingsListItem(
                            title = "Allow notifications",
                            icon = IconSource(Icons.Outlined.Notifications),
                            trailingContent = {
                                Switch(
                                    checked = state.notification?.isNotificationAllowed == true,
                                    onCheckedChange = {
                                        viewModel.onEvent(ManagePrayerEvent.ChangeNotificationsAllowed())
                                    }
                                )
                            },
                            isLastItem = true,
                            onClick = {
                                viewModel.onEvent(ManagePrayerEvent.ChangeNotificationsAllowed())
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
                                            modifier = Modifier.padding(top = if (index == 0) 0.dp else 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                                        )
                                    }

                                    ManagePrayerCard(
                                        time = state.time,
                                        notification = notification,
                                        isEnabled = state.notification.isNotificationAllowed,
                                        isExpanded = state.expandedNotificationIndex == index,
                                        isFirstItem = index == 0 || index == 1,
                                        isDefaultNotification = index == 0,
                                        isLastItem = index == 0 || index == notifications.lastIndex,
                                        isBeforeExpandedItem = index == state.expandedNotificationIndex?.let { item -> item - 1 },
                                        isAfterExpandedItem = index == state.expandedNotificationIndex?.let { item -> item + 1 },
                                        changeOffset = { increment ->
                                            viewModel.onEvent(ManagePrayerEvent.ChangeOffset(notification, increment))
                                        },
                                        changeOffsetRelation = {
                                            viewModel.onEvent(ManagePrayerEvent.ToggleOffsetPolarity(notification))
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