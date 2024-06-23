package com.example.domes.presentation.routes.settings.routes.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domes.domain.model.location.isUnknown
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.button.Variant
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.dialog.InputDialog
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.PrayersViewModel
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeler.sheets.input.models.InputHeader
import com.maxkeppeler.sheets.input.models.InputTextField
import com.maxkeppeler.sheets.input.models.ValidationResult

class UIConstants {
    companion object
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(
    navController: NavController,
    prayersViewModel: PrayersViewModel,
) {
    val state = prayersViewModel.locationState.value

    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Location",
                navigationIcon = {
                    if (state.location != null && navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                bottomActions = listOf {
                    ActionSectionChip(
                        "${state.locale.city}, ${state.locale.countryCode}",
                        Variant.VISUAL,
                        placeholderText = if (state.locale.isUnknown()) "Loading..." else null,
                        icon = IconSource(Icons.Outlined.PinDrop)
                    )
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
                            "Refresh device location",
                            IconSource(Icons.Outlined.Refresh),
                            "Fetch up-to-date location via GPS",
                            onClick = {
                                if (locationPermissionState.status.isGranted) {
                                    prayersViewModel.onEvent(LocationEvent.RefreshLocation)
                                } else {
                                    locationPermissionState.launchPermissionRequest()
                                }
                            },
                        )
                        SettingsListItem(
                            "Auto-update",
                            IconSource(Icons.Outlined.Autorenew),
                            "Fetch location in the background",
                            trailingContent = {
                                Switch(
                                    checked = state.isAutoUpdateEnabled,
                                    onCheckedChange = {
                                        prayersViewModel.onEvent(LocationEvent.ToggleAutoUpdate)
                                    }
                                )
                            },
                            isLastItem = true,
                            onClick = { prayersViewModel.onEvent(LocationEvent.ToggleAutoUpdate) }
                        )
                    }
                }
                item {
                    FixedCard {
                        SettingsListItem(
                            "Map",
                            IconSource(Icons.Outlined.Map),
                            "Manually search for a location",
                            onClick = { },
                        )
                        SettingsListItem(
                            "Coordinates",
                            IconSource(Icons.Outlined.Language),
                            "Manually enter latitude and logitude",
                            isLastItem = true,
                            onClick = {
                                prayersViewModel.onEvent(LocationEvent.SetCoordinatesDialogVisibility())
                            }
                        )
                    }
                }
            }
        }

        if (state.showCoordinatesDialog) {
            InputDialog(
                title = "Coordinates",
                icon = IconSource(Icons.Outlined.Language),
                inputs = listOf(
                    InputTextField(
                        text = state.location?.latitude.toString(),
                        header = InputHeader(
                            title = "Latitude",
                            icon = IconSource(Icons.Outlined.SwapHoriz)
                        ),
                        required = true,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        validationListener = { value ->
                            if ((value?.toDoubleOrNull()
                                    ?: 100.0) <= 50.0 && ((value?.toDoubleOrNull()
                                    ?: -100.0) >= -50.0)
                            ) ValidationResult.Valid
                            else ValidationResult.Invalid("Enter a valid value")
                        }
                    ),
                    InputTextField(
                        text = state.location?.longitude.toString(),
                        header = InputHeader(
                            title = "Longitude",
                            icon = IconSource(Icons.Outlined.SwapVert)
                        ),
                        required = true,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        validationListener = { value ->
                            if ((value?.toDoubleOrNull()
                                    ?: 100.0) <= 50.0 && ((value?.toDoubleOrNull()
                                    ?: -100.0) >= -50.0)
                            ) ValidationResult.Valid
                            else ValidationResult.Invalid("Enter a valid value")
                        }
                    )
                ),
                setDialogVisibility = {
                    prayersViewModel.onEvent(
                        LocationEvent.SetCoordinatesDialogVisibility(
                            false
                        )
                    )
                }
            ) {
                prayersViewModel.onEvent(
                    LocationEvent.ChangeCoordinates(
                        it.getString("0")!!.toDouble(),
                        it.getString("1")!!.toDouble()
                    )
                )
            }
        }
    }
}