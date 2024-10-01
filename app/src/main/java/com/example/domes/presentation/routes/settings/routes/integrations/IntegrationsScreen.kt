package com.example.domes.presentation.routes.settings.routes.integrations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.HomeMini
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domes.presentation.common.composables.card.ExpandableCard
import com.example.domes.presentation.common.composables.listitem.IndicativeTrailingContent
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.maxkeppeker.sheets.core.models.base.IconSource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun IntegrationsScreen(
    viewModel: IntegrationsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
    ) {
        item {
            ExpandableCard(
                content = {
                    SettingsListItem(
                        "Calendar",
                        IconSource(Icons.Outlined.Event),
                        description = "Automatically add prayers to calendar",
                        trailingContent = {
                            IndicativeTrailingContent {
                                Switch(
                                    checked = false,
                                    onCheckedChange = { }
                                )
                            }
                        },
                    )
                },
                expandedContent = {
                    Text("Add prayer times to calendar")
                },
                isFirstItem = true,
                isExpanded = state.expandedSettingIndex == 0,
                expand = { expanded ->
                    viewModel.onEvent(
                        IntegrationsEvent.ChangeExpandedSetting(
                            if (expanded) null else 0
                        )
                    )
                },
            )
        }
        item {
            ExpandableCard(
                isEnabled = false,
                content = {
                    SettingsListItem(
                        "Google Nest",
                        IconSource(Icons.Outlined.HomeMini),
                        description = "Play adhan on Google Nest devices",
                        isLastItem = true,
                        trailingContent = {
                            IndicativeTrailingContent {
                                Switch(
                                    checked = false,
                                    onCheckedChange = { }
                                )
                            }
                        },
                    )
                },
                expand = {}
            )
        }
        item {
            ExpandableCard(
                content = {
                    SettingsListItem(
                        "Health Connect",
                        IconSource(Icons.Outlined.HealthAndSafety),
                        description = "Fetch menstrual cycle status automatically",
                        isLastItem = true,
                        trailingContent = {
                            IndicativeTrailingContent {
                                Switch(
                                    checked = false,
                                    onCheckedChange = { }
                                )
                            }
                        },
                    )
                },
                isEnabled = false,
                isLastItem = true,
                expand = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun AppearancePreview() {
    val viewModel = hiltViewModel<IntegrationsViewModel>()

    IntegrationsScreen(viewModel)
}