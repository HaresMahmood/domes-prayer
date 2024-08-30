package com.example.domes.presentation.routes.settings.routes.location

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.EditLocation
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domes.domain.model.location.isUnknown
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.button.Variant
import com.example.domes.presentation.common.composables.card.ExpandableCard
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.sections.ActionSection
import com.example.domes.presentation.common.composables.listitem.IndicativeTrailingContent
import com.example.domes.presentation.common.composables.listitem.ListItemDivider
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.PrayersEvent
import com.example.domes.presentation.routes.prayers.PrayersViewModel
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.IconSource

class UIConstants {
    companion object
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(
    navController: NavController,
    prayersViewModel: PrayersViewModel,
    locationViewModel: LocationViewModel = hiltViewModel(),
) {
    val state = prayersViewModel.prayersState.value
    val uiState = locationViewModel.state.value

    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar("Location", navigationIcon = {
                if (state.location != null && navController.previousBackStackEntry != null) {
                    IconButton(onClick = {
                        navController.popBackStack(
                            route = Screen.Settings.route,
                            inclusive = false
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            }, bottomActions = listOf {
                ActionSectionChip(
                    "${state.locale.city}, ${state.locale.countryCode}",
                    Variant.VISUAL,
                    placeholderText = if (state.locale.isUnknown()) "Loading..." else null,
                    icon = IconSource(Icons.Outlined.PinDrop)
                )
            }, scrollBehavior = scrollBehavior
            )
        },

        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    FixedCard {
                        SettingsListItem(
                            "Device GPS",
                            IconSource(Icons.Outlined.GpsFixed),
                            "Fetch up-to-date location via GPS",
                            onClick = {
                                if (locationPermissionState.status.isGranted) {
                                    prayersViewModel.onEvent(PrayersEvent.RefreshLocation)
                                } else {
                                    locationPermissionState.launchPermissionRequest()
                                }
                            },
                        )
                        SettingsListItem("Auto-update",
                            IconSource(Icons.Outlined.Autorenew),
                            "Fetch location in the background",
                            trailingContent = {
                                Switch(checked = uiState.isAutoUpdateEnabled, onCheckedChange = {
                                    locationViewModel.onEvent(LocationEvent.ToggleAutoUpdate)
                                })
                            },
                            isLastItem = true,
                            onClick = { locationViewModel.onEvent(LocationEvent.ToggleAutoUpdate) })
                    }
                }
                item {
                    Column {
                        ExpandableCard(
                            content = {
                                SettingsListItem(
                                    "Map",
                                    IconSource(Icons.Outlined.Map),
                                    "Manually search for a location",
                                    trailingContent = {
                                        IndicativeTrailingContent {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                                        }
                                    },
                                    isLastItem = true,
                                )
                            },
                            expandedContent = {
                                ListItemDivider()

                                ActionSection {
                                    FilledTonalButton(
                                        onClick = {
                                            locationViewModel.onEvent(
                                                LocationEvent.ExpandMapsSetting(
                                                    false
                                                )
                                            )
                                        }
                                    ) {
                                        Icon(Icons.Outlined.Done, "Done")
                                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                        Text("Done")
                                    }
                                }
                            },
                            isExpanded = uiState.expandMapsSetting,
                            isFirstItem = true,
                            isBeforeExpandedItem = uiState.expandCoordinatesSetting,
                            expand = {
                                locationViewModel.onEvent(LocationEvent.ExpandMapsSetting())
                            }
                        )
                        ExpandableCard(
                            content = {
                                SettingsListItem(
                                    "Coordinates",
                                    IconSource(Icons.Outlined.Language),
                                    "Manually enter latitude and longitude",
                                    trailingContent = {
                                        IndicativeTrailingContent {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                                        }
                                    },
                                    isLastItem = true,
                                )
                            },
                            expandedContent = {
                                val error =
                                    uiState.showLatitudeTextError || uiState.showLongitudeTextError || uiState.editedLocale?.isUnknown() == true

                                AnimatedVisibility(
                                    uiState.editedLocale != null, modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(start = 16.dp)
                                ) {
                                    uiState.editedLocale?.let {
                                        ActionSectionChip(
                                            if (error) "Invalid coordinates" else "${it.city}, ${it.countryCode}",
                                            if (error) Variant.ERROR else Variant.DEFAULT,
                                            icon = IconSource(Icons.Outlined.EditLocation)
                                        )
                                    }
                                }
                                ListItemDivider()
                                SettingsListItem(
                                    icon = IconSource(Icons.Outlined.SwapHoriz),
                                    trailingContent = {
                                        OutlinedTextField(
                                            modifier = Modifier.fillMaxWidth(),
                                            value = uiState.latitudeText,
                                            label = { Text("Latitude") },
                                            onValueChange = {
                                                if (it.length <= 17)
                                                    locationViewModel.onEvent(
                                                        LocationEvent.OnLatitudeTextChange(it)
                                                    )
                                            },
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    locationViewModel.onEvent(
                                                        LocationEvent.OnLatitudeTextChange("")
                                                    )
                                                }) {
                                                    Icon(Icons.Outlined.Cancel, "Cancel")
                                                }
                                            },
                                            isError = uiState.showLatitudeTextError,
                                            textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true
                                        )
                                    },
                                    isLastItem = true
                                )
                                SettingsListItem(
                                    icon = IconSource(Icons.Outlined.SwapVert),
                                    trailingContent = {
                                        OutlinedTextField(
                                            modifier = Modifier.fillMaxWidth(),
                                            value = uiState.longitudeText,
                                            label = { Text("Latitude") },
                                            onValueChange = {
                                                if (it.length <= 17)
                                                    locationViewModel.onEvent(
                                                        LocationEvent.OnLongitudeTextChange(it)
                                                    )
                                            },
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    locationViewModel.onEvent(
                                                        LocationEvent.OnLongitudeTextChange("")
                                                    )
                                                }) {
                                                    Icon(Icons.Outlined.Cancel, "Cancel")
                                                }
                                            },
                                            isError = uiState.showLongitudeTextError,
                                            textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true
                                        )
                                    },
                                    isLastItem = true
                                )
                                ActionSection {
                                    FilledTonalButton(
                                        enabled = !error && uiState.editedLocale != null,
                                        onClick = {
                                            locationViewModel.onEvent(
                                                LocationEvent.ExpandCoordinatesSetting(
                                                    false
                                                )
                                            )
                                            prayersViewModel.onEvent(
                                                PrayersEvent.ChangeCoordinates(
                                                    uiState.latitudeText.toDouble(),
                                                    uiState.longitudeText.toDouble()
                                                )
                                            )
                                            locationViewModel.onEvent(LocationEvent.ResetCoordinatesText)
                                        }
                                    ) {
                                        Icon(Icons.Outlined.Done, "Done")
                                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                        Text("Done")
                                    }
                                }
                            },
                            isExpanded = uiState.expandCoordinatesSetting,
                            isLastItem = true,
                            isAfterExpandedItem = uiState.expandMapsSetting,
                            expand = { expanded ->
                                if (uiState.latitudeText.isEmpty() && uiState.longitudeText.isEmpty() && state.location != null && !expanded) {
                                    locationViewModel.onEvent(
                                        LocationEvent.PrefillCoordinatesText(
                                            state.location
                                        )
                                    )
                                }

                                locationViewModel.onEvent(LocationEvent.ExpandCoordinatesSetting())
                            },
                        )
                    }
                }
            }
        }

        // TODO: Broken
//        if (state.showCoordinatesDialog) {
//            InputDialog(
//                title = "Coordinates",
//                icon = IconSource(Icons.Outlined.Language),
//                inputs = listOf(
//                    InputTextField(
//                        text = state.location?.latitude.toString(),
//                        header = InputHeader(
//                            title = "Latitude",
//                            icon = IconSource(Icons.Outlined.SwapHoriz)
//                        ),
//                        required = true,
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                        validationListener = { value ->
//                            if ((value?.toDoubleOrNull()
//                                    ?: 100.0) <= 50.0 && ((value?.toDoubleOrNull()
//                                    ?: -100.0) >= -50.0)
//                            ) ValidationResult.Valid
//                            else ValidationResult.Invalid("Enter a valid value")
//                        }
//                    ),
//                    InputTextField(
//                        text = state.location?.longitude.toString(),
//                        header = InputHeader(
//                            title = "Longitude",
//                            icon = IconSource(Icons.Outlined.SwapVert)
//                        ),
//                        required = true,
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                        validationListener = { value ->
//                            if ((value?.toDoubleOrNull()
//                                    ?: 100.0) <= 50.0 && ((value?.toDoubleOrNull()
//                                    ?: -100.0) >= -50.0)
//                            ) ValidationResult.Valid
//                            else ValidationResult.Invalid("Enter a valid value")
//                        }
//                    )
//                ),
//                setDialogVisibility = {
//                    viewModel.onEvent(
//                        LocationEvent.SetCoordinatesDialogVisibility(
//                            false
//                        )
//                    )
//                }
//            ) {
//                viewModel.onEvent(
//                    LocationEvent.ChangeCoordinates(
//                        it.getString("0")!!.toDouble(),
//                        it.getString("1")!!.toDouble()
//                    )
//                )
//            }
//        }
    }
}