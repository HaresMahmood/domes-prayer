package com.example.domes.presentation.routes.settings.routes.calculation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.RestartAlt
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Madhab
import com.example.domes.presentation.common.composables.LayeredTopAppBar
import com.example.domes.presentation.common.composables.dialog.OptionDialog
import com.example.domes.presentation.common.composables.dialog.OptionItem
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import com.example.domes.presentation.routes.settings.routes.notifications.CalculationEvent
import com.example.domes.presentation.routes.settings.routes.notifications.NotificationsViewModel
import com.example.domes.ui.theme.DomesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeler.sheets.option.models.Option

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun CalculationScreen(
//    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val calculationMethodOptions = listOf(
        Pair(
            Option(
                titleText = "Muslim World League",
                customView = { selected ->
                    OptionItem(
                        "Muslim World League",
                        "Fajr at ${CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters.fajrAngle}°, Isha at ${CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.MUSLIM_WORLD_LEAGUE
            ), CalculationMethod.MUSLIM_WORLD_LEAGUE
        ),
        Pair(
            Option(
                titleText = "Egypt",
                customView = { selected ->
                    OptionItem(
                        "Egypt",
                        "Egyptian General Authority of Survey • Fajr at ${CalculationMethod.EGYPTIAN.parameters.fajrAngle}°, Isha at ${CalculationMethod.EGYPTIAN.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.EGYPTIAN
            ), CalculationMethod.EGYPTIAN
        ),
        Pair(
            Option(
                titleText = "Karachi",
                customView = { selected ->
                    OptionItem(
                        "Karachi",
                        "University of Islamic Sciences, Karachi • Fajr at ${CalculationMethod.KARACHI.parameters.fajrAngle}°, Isha at ${CalculationMethod.KARACHI.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.KARACHI
            ), CalculationMethod.KARACHI
        ),
        Pair(
            Option(
                titleText = "Umm al-Qura",
                customView = { selected ->
                    OptionItem(
                        "Umm al-Qura",
                        "Umm al-Qura University, Makkah • Fajr at ${CalculationMethod.UMM_AL_QURA.parameters.fajrAngle}°, Isha at ${CalculationMethod.UMM_AL_QURA.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.UMM_AL_QURA
            ), CalculationMethod.UMM_AL_QURA
        ),
        Pair(
            Option(
                titleText = "Dubai",
                customView = { selected ->
                    OptionItem(
                        "Dubai",
                        "The Gulf Region • Fajr at ${CalculationMethod.DUBAI.parameters.fajrAngle}°, Isha at ${CalculationMethod.DUBAI.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.DUBAI
            ), CalculationMethod.DUBAI
        ),
        Pair(
            Option(
                titleText = "Moonsighting Committee",
                customView = { selected ->
                    OptionItem(
                        "Moonsighting Committee",
                        "Fajr at ${CalculationMethod.MOON_SIGHTING_COMMITTEE.parameters.fajrAngle}°, Isha at ${CalculationMethod.MOON_SIGHTING_COMMITTEE.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.MOON_SIGHTING_COMMITTEE
            ), CalculationMethod.MOON_SIGHTING_COMMITTEE
        ),
        Pair(
            Option(
                titleText = "North America",
                customView = { selected ->
                    OptionItem(
                        "North America",
                        "Referred to as the ISNA method • This method is included for completeness, but is not recommended • Fajr at ${CalculationMethod.NORTH_AMERICA.parameters.fajrAngle}°, Isha at ${CalculationMethod.NORTH_AMERICA.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.NORTH_AMERICA
            ), CalculationMethod.NORTH_AMERICA
        ),
        Pair(
            Option(
                titleText = "Kuwait",
                customView = { selected ->
                    OptionItem(
                        "Kuwait",
                        "Fajr at ${CalculationMethod.KUWAIT.parameters.fajrAngle}°, Isha at ${CalculationMethod.KUWAIT.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.KUWAIT
            ), CalculationMethod.KUWAIT
        ),
        Pair(
            Option(
                titleText = "Qatar",
                customView = { selected ->
                    OptionItem(
                        "Qatar",
                        "Fajr at ${CalculationMethod.QATAR.parameters.fajrAngle}°, Isha at ${CalculationMethod.QATAR.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.QATAR
            ), CalculationMethod.QATAR
        ),
        Pair(
            Option(
                titleText = "Singapore",
                customView = { selected ->
                    OptionItem(
                        "Singapore",
                        "Fajr at ${CalculationMethod.SINGAPORE.parameters.fajrAngle}°, Isha at ${CalculationMethod.SINGAPORE.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.SINGAPORE
            ), CalculationMethod.SINGAPORE
        ),
        Pair(
            Option(
                titleText = "Turkey",
                customView = { selected ->
                    OptionItem(
                        "Turkey",
                        "Fajr at ${CalculationMethod.TURKEY.parameters.fajrAngle}°, Isha at ${CalculationMethod.TURKEY.parameters.ishaAngle}°",
                        selected = selected
                    )
                },
                selected = state.calculationMethod == CalculationMethod.TURKEY
            ), CalculationMethod.TURKEY
        ),
    )

    val juristicOptions = listOf(
        Pair(
            Option(
                titleText = "Shafi'i • Maliki • Hambali",
                customView = { selected ->
                    OptionItem(
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
                    OptionItem(
                        "Hanafi",
                        "Begins when the shadow of an object twice its actual length, plus it's shadow during noon",
                        overline = "LATER ASR",
                        selected = selected
                    )
                },
                selected = state.juristic == Madhab.HANAFI,
//                disabled = state.juristic == Madhab.HANAFI
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
                                    viewModel.onEvent(CalculationEvent.SetCalculationMethodDialogVisibility())
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
                                    calculationMethodOptions[state.calculationMethod.ordinal].first.titleText,
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
                                    viewModel.onEvent(CalculationEvent.SetJuristicDialogVisibility())
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
                item {
                    Text(
                        "Manual adjustments",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 20.dp)
                    )
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
                        state.prayerTimes.forEach { prayerTime ->
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
                                        painterResource(prayerTime.prayer.iconResId),
                                        "Prayer time",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                },
                                headlineContent = { Text(prayerTime.prayer.name) },
                                supportingContent = {
                                    Text(
                                        prayerTime.time.format(getFormatter("HH:mm a")),
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                },
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(35.dp)
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
                                    viewModel.onEvent(CalculationEvent.ResetSettings())
                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                                headlineColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.RestartAlt,
                                    "Reset to default",
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            },
                            headlineContent = { Text("Reset to default") },
                        )
                    }
                }
            }
        }

        if (state.showJuristicDialog) {
            OptionDialog(
                title = "Juristic",
                icon = IconSource(Icons.Outlined.AccountBalance),
                options = juristicOptions.map { it.first },
                setDialogVisibility = { isVisible ->
                    viewModel.onEvent(CalculationEvent.SetJuristicDialogVisibility(isVisible))
                }
            ) { index, _ ->
                viewModel.onEvent(CalculationEvent.ChangeJuristic(juristicOptions[index].second))
            }
        }

        if (state.showCalculationMethodDialog) {
            OptionDialog(
                title = "Calculation method",
                icon = IconSource(Icons.Outlined.Calculate),
                options = calculationMethodOptions.map { it.first },
                setDialogVisibility = { isVisible ->
                    viewModel.onEvent(
                        CalculationEvent.SetCalculationMethodDialogVisibility(
                            isVisible
                        )
                    )
                }
            ) { index, _ ->
                viewModel.onEvent(CalculationEvent.ChangeCalculationMethod(calculationMethodOptions[index].second))
            }
        }
    }
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