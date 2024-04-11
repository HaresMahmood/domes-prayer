package com.example.domes.presentation.settings.routes.calculation

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Madhab
import com.example.domes.presentation.common.composables.LayeredTopAppBar
import com.example.domes.ui.theme.DomesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CalculationScreen(
//    navController: NavController,
    viewModel: CalculationViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val juristicOptions = listOf(
        Pair(
            Option(
                titleText = "Shafi'i • Maliki • Hambali",
                customView = { selected ->
                    OptionView(
                        "Shafi'i • Maliki • Hambali",
                        "Begins when the shadow of an object equals it's actual length, plus its shadow during noon",
                        overline = "EARLIER ASR",
                        selected = selected
                    )
                },
                selected = state.juristic == Madhab.SHAFI
            ), Madhab.SHAFI
        ),
        Pair(
            Option(
                titleText = "Hanifi",
                customView = { selected ->
                    OptionView(
                        "Hanafi",
                        "Begins when the shadow of an object twice its actual length, plus it's shadow during noon",
                        overline = "LATER ASR",
                        selected = selected
                    )
                },
                selected = state.juristic == Madhab.HANAFI
            ), Madhab.HANAFI
        ),
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Calculation",
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
                        Text("State")
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

                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Calculate,
                                    "Calculation method",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Calculation method") },
                            supportingContent = {
                                Text(
                                    "Muslim World League",
                                    color = MaterialTheme.colorScheme.tertiary
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
                                    viewModel.onEvent(CalculationEvent.ToggleJuristicDialogVisibility())
                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.AccountBalance,
                                    "Juristic",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Juristic") },
                            supportingContent = {
                                Text(
                                    juristicOptions[state.juristic.ordinal].first.titleText,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            },
                        )
                    }
                }
            }
        }

        if (state.showJuristicDialog) {
            JuristicDialog(
                options = juristicOptions.map { it.first },
                onDismiss = { viewModel.onEvent(CalculationEvent.ToggleJuristicDialogVisibility()) }
            ) { index, _ ->
                viewModel.onEvent(CalculationEvent.ChangeJuristic(juristicOptions[index].second))
            }
        }

        println("JURISTIC: ${state.showJuristicDialog}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CalculationMethodDialog(
    onSelection: () -> Unit
) {
    val options = CalculationMethod.entries.map {
        Option(
            titleText = it.name,
//            subtitleText = it.parameters.toString(),
            selected = it == CalculationMethod.MUSLIM_WORLD_LEAGUE
        )
    }

    OptionDialog(
        state = rememberUseCaseState(visible = true),
        selection = OptionSelection.Single(options) { index, option ->
            onSelection()
        },
        config = OptionConfig(mode = DisplayMode.LIST)
    )
}

@Composable
internal fun OptionView(
    title: String,
    description: String,
    selected: Boolean,
    overline: String? = null
) {
    ListItem(
        overlineContent = {
            overline?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        headlineContent = {
            Text(
                title,
                color = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            )
        },
        supportingContent = {
            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = if (selected) MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp) else Color.Transparent
        ),
        tonalElevation = 5.dp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun JuristicDialog(
    options: List<Option>,
    onDismiss: () -> Unit,
    onSelection: (index: Int, option: Option) -> Unit
) {
    OptionDialog(
        state = rememberUseCaseState(
            visible = true,
            onCloseRequest = { onDismiss() },
        ),
        header = Header.Default("Juristic", IconSource(Icons.Outlined.AccountBalance)),
        selection = OptionSelection.Single(
            options,
            positiveButton = SelectionButton(
                "Done",
                IconSource(Icons.Outlined.Done),
                ButtonStyle.FILLED
            ),
            negativeButton = SelectionButton(
                "Cancel",
                IconSource(Icons.Outlined.Close),
                ButtonStyle.OUTLINED
            ),
            onSelectOption = onSelection,
        ),
        config = OptionConfig(mode = DisplayMode.LIST)
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        CalculationScreen()
    }
}