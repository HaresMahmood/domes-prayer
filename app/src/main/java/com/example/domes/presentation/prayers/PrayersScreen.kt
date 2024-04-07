package com.example.domes.presentation.prayers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domes.presentation.prayers.UIConstants.Companion.getArabicFormatter
import com.example.domes.presentation.prayers.UIConstants.Companion.getFormatter
import com.example.domes.presentation.prayers.composables.DisplaySlider
import com.example.domes.presentation.prayers.composables.LayeredTopAppBar
import com.example.domes.presentation.prayers.composables.MainActionSection
import com.example.domes.presentation.prayers.composables.PercentageWidthBox
import com.example.domes.presentation.prayers.composables.PercentageWidthBoxChild
import com.example.domes.presentation.prayers.composables.PrayerListItem
import com.example.domes.presentation.prayers.composables.SelectedDateBottomSheetSection
import com.example.domes.presentation.util.Polygon
import com.example.domes.presentation.util.ShapeParameters
import com.example.domes.ui.theme.DomesTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PrayersScreen(
    viewModel: PrayersViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val prayerTimes = if (state.selectedDate == LocalDate.now()) state.currentPrayerTimes else state.selectedPrayerTimes

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
        sheetPeekHeight = UIConstants.SHEET_PEEK_HEIGHT,
        sheetShape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
        modifier = Modifier.animateContentSize(),
        sheetDragHandle = { },
        sheetContent = {
            SelectedDateBottomSheetSection(
                gregorianDate = state.selectedDate,
                hijriDate = state.selectedHijriDate,
                bottomSheetExpandAction = { scope.launch { scaffoldState.bottomSheetState.expand() } },
                bottomSheetCollapseAction = { scope.launch { scaffoldState.bottomSheetState.partialExpand() } }
            ) { selectedDate ->
                viewModel.onEvent(PrayersEvent.ChangeDate(selectedDate))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    val foregroundText = state.currentPrayer?.prayer?.name ?: state.selectedDate.format(getFormatter("d MMMM"))
                    val backgroundText = state.currentPrayer?.prayer?.arabicName ?: getArabicFormatter("MMM d").format(state.selectedHijriDate)

                    LayeredTopAppBar(foregroundText, backgroundText, scrollBehavior)
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { },
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
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        stickyHeader {
                            MainActionSection(state.timeLeft, viewModel.calculateRemainingPrayerTime(), state.city) {
                                //viewModel.onEvent(PrayersEvent.Snooze)
                            }
                        }
                        item {
                            val shapeParam = ShapeParameters(
                                roundness = .4f,
                                sides = 4,
                                rotation = 90f,
                                shapeId = ShapeParameters.ShapeId.Polygon
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
                                        children = prayerTimes.filter { it.prayer.displayable }.map { prayerTime ->
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
                                        isCurrent = prayerTime == state.currentPrayer,
                                        hasPassed = prayerTime.timeWindow.isBefore(LocalDateTime.now()) && prayerTime != state.currentPrayer,
                                        isSilent = false,
                                        isFirstItem = index == 0,
                                        isLastItem = index == prayerTimes.lastIndex,
                                        calculateProgress = { viewModel.calculateRemainingPrayerTime() },
                                        onClick = { },
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

@RequiresApi(Build.VERSION_CODES.S)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        PrayersScreen()
    }
}