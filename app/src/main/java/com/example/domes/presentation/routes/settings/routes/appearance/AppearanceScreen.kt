package com.example.domes.presentation.routes.settings.routes.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.button.SegmentedButtonItem
import com.example.domes.presentation.common.composables.button.SegmentedButtons
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.maxkeppeker.sheets.core.models.base.IconSource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AppearanceScreen(
    navController: NavController,
//    viewModel: AppearanceViewModel = hiltViewModel()
) {
//    val state = viewModel.state.value

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Appearance",
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
                    FixedCard {
                        SettingsListItem(
                            "Theme",
                            IconSource(Icons.Outlined.Opacity),
                            description = "Custom",
                            extendedContent = {
                                SegmentedButtons {
                                    SegmentedButtonItem(
                                        selected = true,
                                        onClick = {},
//                                        icon = { Icons.Outlined.BrightnessAuto },
                                        label = { Text("System") },
                                    )
                                    SegmentedButtonItem(
                                        selected = false,
                                        onClick = {},
//                                        icon = { Icons.Outlined.Brightness1 },
                                        label = { Text("Light") },
                                    )
                                    SegmentedButtonItem(
                                        selected = false,
                                        onClick = {},
//                                        icon = { Icons.Outlined.Brightness2 },
                                        label = { Text("Dark") },
                                    )
                                }
                            },
                            isLastItem = true
                        )
                    }
                }
                item {
                    Text(
                        "General",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                }
                item {
                    FixedCard {
                        SettingsListItem(
                            "Language",
                            IconSource(Icons.Outlined.CalendarMonth),
                            value = "System default • English",
                            isLastItem = true
                        )
                    }
                }
                item {
                    FixedCard {
                        SettingsListItem(
                            "Time",
                            IconSource(Icons.Outlined.AccessTime),
                            value = "12-hour clock"
                        )
                        SettingsListItem(
                            "Date",
                            IconSource(Icons.Outlined.CalendarMonth),
                            value = "Month and day",
                            isLastItem = true
                        )
                    }
                }
                item {
                    FixedCard {
                        SettingsListItem(
                            "Haptic feedback",
                            IconSource(Icons.Outlined.Vibration),
                            description = "Pair interactions with haptic feedback when in vibration mode",
                            isLastItem = true,
                            trailingContent = {
                                Switch(
                                    checked = false,
                                    onCheckedChange = { }
                                )
                            },
                        )
                    }
                }
                item {
                    Text(
                        "Prayers sceen",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(
                            top = 0.dp,
                            bottom = 20.dp,
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                    FixedCard {
                        SettingsListItem(
                            "Main date format",
                            IconSource(Icons.Outlined.CalendarMonth),
                            description = "Switch Gregorian and Hijri dates",
                            trailingContent = {
                                Switch(
                                    checked = false,
                                    onCheckedChange = { }
                                )
                            },
                        )
                        SettingsListItem(
                            "Quick date switching",
                            IconSource(Icons.Outlined.CalendarMonth),
                            description = "Allow going forward and backward by swiping left and right",
                            isLastItem = true,
                            trailingContent = {
                                Switch(
                                    checked = false,
                                    onCheckedChange = { }
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun AppearancePreview() {
    val navController = rememberNavController()

    AppearanceScreen(navController)
}