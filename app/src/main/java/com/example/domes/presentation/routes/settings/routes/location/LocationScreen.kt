package com.example.domes.presentation.routes.settings.routes.location

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domes.presentation.common.composables.LayeredTopAppBar
import com.example.domes.presentation.common.composables.dialog.InputDialog
import com.example.domes.ui.theme.DomesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeler.sheets.input.models.InputHeader
import com.maxkeppeler.sheets.input.models.InputTextField
import com.maxkeppeler.sheets.input.models.ValidationResult

class UIConstants {
    companion object {

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(
//    navController: NavController,
    viewModel: LocationViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Location",
                navigationIcon = Icons.AutoMirrored.Outlined.ArrowBack,
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
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(
                            topStartPercent = 12,
                            topEndPercent = 45,
                            bottomStartPercent = 45,
                            bottomEndPercent = 45
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        ),
                    ) {
                        Icon(Icons.Outlined.PinDrop, "Current location")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(state.locale)
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceColorAtElevation(
                                    1.dp
                                ), RoundedCornerShape(35.dp)
                            ),
                    ) {
                        // TODO: Create unified ListItem composable
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 5.dp,
                                    end = 5.dp,
                                )
                                .clickable {
                                    if (locationPermissionState.status.isGranted) {
                                        viewModel.onEvent(LocationEvent.RefreshLocation)
                                    } else {
                                        locationPermissionState.launchPermissionRequest()
                                    }
                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Refresh,
                                    "Setting icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Refresh device location") },
                            supportingContent = {
                                Text(
                                    "Fetch up-to-date location via GPS",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                        )
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 5.dp,
                                    end = 5.dp,
                                    bottom = 8.dp,
                                )
                                .clickable { viewModel.onEvent(LocationEvent.ToggleAutoUpdate) },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Autorenew,
                                    "Setting icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Auto-update") },
                            supportingContent = {
                                Text(
                                    "Fetch location in the background",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingContent = {
                                Switch(
                                    checked = state.isAutoUpdateEnabled,
                                    onCheckedChange = { viewModel.onEvent(LocationEvent.ToggleAutoUpdate) }
                                )
                            }
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceColorAtElevation(
                                    1.dp
                                ), RoundedCornerShape(35.dp)
                            ),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 5.dp,
                                    end = 5.dp,
                                ),
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Map,
                                    "Setting icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Map") },
                            supportingContent = {
                                Text(
                                    "Manually search for a location",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                        )
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 5.dp,
                                    end = 5.dp,
                                    bottom = 8.dp,
                                )
                                .clickable {
                                           viewModel.onEvent(LocationEvent.SetCoordinatesDialogVisibility())
                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Language,
                                    "Setting icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Coordinates") },
                            supportingContent = {
                                Text(
                                    "Manually enter latitude and logitude",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
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
                        header = InputHeader(
                            title = "Latitude",
                            icon = IconSource(Icons.Outlined.SwapHoriz)
                        ),
                        required = true,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        validationListener = { value ->
                            if ((value?.toDoubleOrNull() ?: 100.0) <= 50.0 && ((value?.toDoubleOrNull() ?: -100.0) >= -50.0)) ValidationResult.Valid
                            else ValidationResult.Invalid("Enter a valid value")
                        }
                    ),
                    InputTextField(
                        header = InputHeader(
                            title = "Longitude",
                            icon = IconSource(Icons.Outlined.SwapVert)
                        ),
                        required = true,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        validationListener = { value ->
                            if ((value?.toDoubleOrNull() ?: 100.0) <= 50.0 && ((value?.toDoubleOrNull() ?: -100.0) >= -50.0)) ValidationResult.Valid
                            else ValidationResult.Invalid("Enter a valid value")
                        }
                    )
                ),
                setDialogVisibility = { viewModel.onEvent(
                    LocationEvent.SetCoordinatesDialogVisibility(
                        false
                    )
                ) }
            ) {
                viewModel.onEvent(
                    LocationEvent.ChangeCoordinates(
                        it.getString("0")!!.toDouble(),
                        it.getString("1")!!.toDouble()
                    )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        LocationScreen()
    }
}