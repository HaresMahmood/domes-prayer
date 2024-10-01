package com.example.domes.presentation.routes.settings.routes.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Brightness1
import androidx.compose.material.icons.outlined.BrowseGallery
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.common.composables.button.SegmentedButtonValues
import com.example.domes.presentation.common.composables.button.SegmentedButtons
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.maxkeppeker.sheets.core.models.base.IconSource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AppearanceScreen(
//    viewModel: AppearanceViewModel = hiltViewModel()
) {
//    val state = viewModel.state.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
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
                    "Theme",
                    IconSource(Icons.Outlined.Opacity),
                    description = "Custom",
                    extendedContent = {
                        SegmentedButtons(
                            overflow = true,
                            content = listOf(
                                SegmentedButtonValues(
                                    selected = true,
                                    onClick = {},
                                    icon = IconSource(Icons.Outlined.AutoAwesome),
                                    label = { Text("System") },
                                ),
                                SegmentedButtonValues(
                                    selected = false,
                                    onClick = {},
                                    icon = IconSource(Icons.Outlined.Brightness1),
                                    label = { Text("Light") },
                                ),
                                SegmentedButtonValues(
                                    selected = false,
                                    onClick = {},
                                    icon = IconSource(Icons.Filled.Brightness1),
                                    label = { Text("Dark") },
                                )
                            )
                        )
                    },
                    isLastItem = true
                )
            }
        }
        item {
            FixedCard {
                SettingsListItem(
                    "Language",
                    IconSource(Icons.Outlined.Translate),
                    value = "English",
                    extendedContent = {
                        SegmentedButtons(
                            overflow = true,
                            content = listOf(
                                SegmentedButtonValues(
                                    selected = true,
                                    onClick = {},
                                    icon = IconSource(Icons.Outlined.AutoAwesome),
                                    label = { Text("System") },
                                ),
                                SegmentedButtonValues(
                                    selected = false,
                                    onClick = {},
                                    label = { Text("English") },
                                ),
                                SegmentedButtonValues(
                                    selected = false,
                                    onClick = {},
                                    label = { Text("Arabic") },
                                )
                            )
                        )
                    },
                    isLastItem = true
                )
            }
        }
        item {
            FixedCard {
                SettingsListItem(
                    "Haptic feedback",
                    IconSource(Icons.Outlined.Vibration),
                    description = "Pair interactions with haptics",
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
                "Prayers screen",
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
                    "Time format",
                    IconSource(Icons.Outlined.AccessTime),
                    value = "18:20 pm",
                    extendedContent = {
                        SegmentedButtons(
                            overflow = true,
                            content = listOf(
                                SegmentedButtonValues(
                                    selected = false,
                                    onClick = {},
                                    icon = IconSource(Icons.Outlined.AutoAwesome),
                                    label = { Text("System") },
                                ),
                                SegmentedButtonValues(
                                    selected = true,
                                    onClick = {},
                                    icon = IconSource(Icons.Outlined.HistoryToggleOff),
                                    label = { Text("12-hour") },
                                ),
                                SegmentedButtonValues(
                                    selected = false,
                                    onClick = {},
                                    icon = IconSource(Icons.Outlined.BrowseGallery),
                                    label = { Text("24-hour") },
                                )
                            )
                        )
                    },
                )
                SettingsListItem(
                    "Main calendar system",
                    IconSource(Icons.Outlined.CalendarMonth),
                    description = "Calendar system more commonly shown in date selection",
                    extendedContent = {
                        SegmentedButtons(
                            content = listOf(
                                SegmentedButtonValues(
                                    selected = true,
                                    onClick = {},
//                                    icon = IconSource(Icons.Outlined.Public),
                                    label = { Text("Gregorian") },
                                ),
                                SegmentedButtonValues(
                                    selected = false,
                                    onClick = {},
//                                    icon = IconSource(Icons.Outlined.DarkMode),
                                    label = { Text("Hijri") },
                                )
                            )
                        )
                    }
                )
                SettingsListItem(
                    "Quick date switching",
                    IconSource(Icons.Outlined.CalendarMonth),
                    description = "Allow going forward and backward by swiping left and right",
                    trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = { }
                        )
                    },
                    isLastItem = true
                )
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
    AppearanceScreen()
}