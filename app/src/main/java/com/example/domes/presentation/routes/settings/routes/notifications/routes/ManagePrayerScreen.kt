package com.example.domes.presentation.routes.settings.routes.notifications.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.routes.notifications.routes.composables.ManagePrayerCard

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (state.notification?.isNotificationAllowed == true) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceColorAtElevation(
                                    1.dp
                                ),
                                RoundedCornerShape(35.dp)
                            ),
                    ) {
                        // TODO: Add confirmation dialog
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 5.dp,
                                    end = 5.dp,
                                    bottom = 8.dp
                                )
                                .clickable {
//                                    viewModel.onEvent(CalculationEvent.ResetSettings())
                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                                headlineColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Notifications,
                                    "Notifications",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Allow notifications") },
                            trailingContent = {
                                Switch(
                                    checked = state.notification?.isNotificationAllowed == true,
                                    onCheckedChange = {
                                        viewModel.onEvent(ManagePrayerEvent.ChangeNotificationsAllowed())
                                    }
                                )
                            }
                        )
                    }
                }
                item {
                    Text(
                        "Default",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
                item {
                    ManagePrayerCard(
                        isDefaultNotification = true,
                        isExpanded = state.expandedNotificationIndex == -1,
                        onExpand = {
                            viewModel.onEvent(ManagePrayerEvent.ChangeExpandedNotification(if (it) null else -1))
                        }
                    )
                }
                state.notification?.notifications?.let {
                    if (it.size > 0) {
                        item {
                            Text(
                                "Additional",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                        item {
                            Column {
                                it.forEachIndexed { index, notification ->
                                    ManagePrayerCard(
                                        isExpanded = state.expandedNotificationIndex == index,
                                        onExpand = { expanded ->
                                            viewModel.onEvent(
                                                ManagePrayerEvent.ChangeExpandedNotification(
                                                    if (expanded) null else index
                                                )
                                            )
                                        },
                                        onRemove = {
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