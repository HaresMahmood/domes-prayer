package com.example.domes.presentation.routes.prayers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.ExpandCircleDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.shape.Polygon
import com.example.domes.presentation.common.composables.shape.Shape
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.composables.DisplaySlider
import com.example.domes.presentation.routes.prayers.composables.MainActionSection
import com.example.domes.presentation.routes.prayers.composables.PercentageWidthBox
import com.example.domes.presentation.routes.prayers.composables.PercentageWidthBoxChild
import com.example.domes.presentation.routes.prayers.composables.PrayerListItem
import com.example.domes.presentation.routes.prayers.composables.SelectedDateSection
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class UIConstants {
    companion object {
        fun getFormatter(pattern: String): DateTimeFormatter {
            return DateTimeFormatter.ofPattern(pattern)
        }

        fun getArabicFormatter(pattern: String): DateTimeFormatter {
            return DateTimeFormatter.ofPattern(pattern).withLocale(Locale("ar"))
        }

        val SHEET_PEEK_HEIGHT = 100.dp
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun PrayersScreen(
    navController: NavController,
    viewModel: PrayersViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val prayerTimes =
        if (state.selectedDate == LocalDate.now()) state.currentPrayerTimes else state.selectedPrayerTimes

    val scope = rememberCoroutineScope()
    val bottomSheetShaffoldState = rememberBottomSheetScaffoldState()

    val bottomSheetProgress = 1f // (bottomSheetHeight.value / screenHeight.value + 0.37f) * 0.73f

    val pullRefreshState = rememberPullRefreshState(refreshing = state.isRefreshing, onRefresh = {
        viewModel.onEvent(
            PrayersEvent.RefreshLocation
        )
    })

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    BottomSheetScaffold(
        scaffoldState = bottomSheetShaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
        sheetPeekHeight = UIConstants.SHEET_PEEK_HEIGHT,
        sheetShape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
        sheetDragHandle = {},
        sheetContent = {
            SelectedDateSection(
                gregorianDate = state.selectedDate,
                hijriDate = state.selectedHijriDate,
                bottomSheetExpandAction = { scope.launch { bottomSheetShaffoldState.bottomSheetState.expand() } },
                collapseBottomSheet = { scope.launch { bottomSheetShaffoldState.bottomSheetState.partialExpand() } }
            ) { selectedDate ->
                viewModel.onEvent(PrayersEvent.ChangeDate(selectedDate))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .clickable {
                    if (bottomSheetShaffoldState.bottomSheetState.hasExpandedState) {
                        scope.launch { bottomSheetShaffoldState.bottomSheetState.partialExpand() }
                    }
                }
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState)
                .graphicsLayer {
                    // TODO: Unoptimised
                    if (bottomSheetShaffoldState.bottomSheetState.hasExpandedState) {
                        // val from 1959.0.dp to 863.0.dp
                        val bottomSheetHeight = bottomSheetShaffoldState.bottomSheetState.requireOffset().roundToInt()

                        // Map the bottomSheetHeight to a range of 0f to 1f
                        val progress = (bottomSheetHeight - 863) / (1959 - 863).toFloat()

                        // Interpolate the blurRadius, alpha, and scale values based on the progress
                        val blurRadius = lerp(start = 20f, stop = 1f, fraction = progress)
                        val alpha = lerp(start = 0.5f, stop = 1f, fraction = progress)
                        val scale = lerp(start = 0.95f, stop = 1f, fraction = progress)

                        this.alpha = alpha
                        this.scaleX = scale
                        this.scaleY = scale
                        this.renderEffect = BlurEffect(blurRadius, blurRadius)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    val (foregroundText, backgroundText) = if (state.selectedDate == LocalDate.now()) {
                        (state.currentPrayer?.prayer?.name ?: "") to (state.currentPrayer?.prayer?.arabicName ?: "")
                    } else {
                        state.selectedDate.format(UIConstants.getFormatter("d MMMM")) to
                        state.selectedHijriDate.format(UIConstants.getArabicFormatter("MMM d"))
                    }

                    LayeredTopAppBar(
                        foregroundText,
                        backgroundText,
                        topActions = {
                            IconButton(
                                onClick = { viewModel.onEvent(PrayersEvent.SetOverarchingDropdownVisibility()) }
                            ) {
                                Icon(
                                    Icons.Outlined.ExpandCircleDown,
                                    contentDescription = "More options"
                                )
                            }

                            DropdownMenu(
                                expanded = state.showOverarchingDropdown,
                                onDismissRequest = {
                                    viewModel.onEvent(
                                        PrayersEvent.SetOverarchingDropdownVisibility(
                                            false
                                        )
                                    )
                                }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = {
                                        navController.navigate(Screen.Settings.route)
                                        viewModel.onEvent(
                                            PrayersEvent.SetOverarchingDropdownVisibility(
                                                false
                                            )
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Settings,
                                            contentDescription = "Settings"
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Help") },
                                    onClick = { /* Handle edit! */ },
                                    leadingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Outlined.HelpOutline,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        },
                        bottomActions = MainActionSection(
                            state.timeLeft,
                            viewModel.calculateRemainingPrayerTime(),
                            state.locale,
                            navController = navController,
                        ) {
                            //viewModel.onEvent(PrayersEvent.Snooze)
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.Qibla.route)
                        },
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                        )
                    ) {
                        Icon(Icons.Filled.Explore, "Find Qibla")
                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        item {
                            val shapeParam = Shape(
                                roundness = .4f,
                                sides = 4,
                                rotation = 90f,
                                shapeId = Shape.ShapeId.Polygon
                            )
                            val shape = shapeParam.genShape().normalized()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                                            1.dp
                                        ), RoundedCornerShape(35.dp)
                                    ),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
//
//                                        IconToggleButton(
//                                            onCheckedChange = { /* TODO */ },
//                                            checked = true,
//                                            colors = IconButtonDefaults.iconToggleButtonColors(
//                                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                                            ),
//                                            modifier = Modifier
////                                                .padding(end = 8.dp, top = 8.dp),
//                                        ) {
//                                            Icon(
//                                                Icons.Outlined.ViewDay,
//                                                "Location"
//                                            )
//                                        }
//                                    }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 16.dp,
                                            start = 24.dp,
                                            end = 24.dp,
                                            bottom = 16.dp
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DisplaySlider(
                                        sliderValue = state.dayFraction,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        onValueChange = { }
                                    )

                                    PercentageWidthBox(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(20.dp),
                                        children = prayerTimes.filter { it.prayer.displayable }
                                            .map { prayerTime ->
                                                PercentageWidthBoxChild(
                                                    composable = {
                                                        Box(modifier = Modifier.size(10.dp)) {
                                                            Polygon(
                                                                shape,
                                                                color = if (prayerTime == state.currentPrayer) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSecondary,
                                                                alpha = if (prayerTime == state.currentPrayer) 1f else 0.75f
                                                            )
                                                        }
                                                    },
                                                    widthFraction = prayerTime.dayFraction?.toFloat()
                                                )
                                            }
                                    )
                                }
                            }
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth() // TODO: Why?
                                    .background(
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                                            1.dp
                                        ), RoundedCornerShape(35.dp)
                                    ),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                prayerTimes.forEachIndexed { index, prayerTime ->
                                    PrayerListItem(
                                        prayerTime = prayerTime,
                                        hasPrayed = viewModel.mapPrayerToPrayerRecord(prayerTime),
                                        isCurrent = prayerTime == state.currentPrayer,
                                        hasPassed = prayerTime.timeWindow.isBefore(LocalDateTime.now()) && prayerTime != state.currentPrayer,
                                        isSilent = false,
                                        isFirstItem = index == 0,
                                        isLastItem = index == prayerTimes.lastIndex,
                                        remainingTimePercentage = viewModel.calculateRemainingPrayerTime(),
                                        onTick = {
                                            viewModel.onEvent(
                                                PrayersEvent.ChangePrayerRecord(
                                                    prayerTime.prayer,
                                                    it
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                state.isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.S)
//@Preview(
//    showBackground = true,
//    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
//)
//@Composable
//fun DomesPreview() {
//    DomesTheme {
//        PrayersScreen()
//    }
//}