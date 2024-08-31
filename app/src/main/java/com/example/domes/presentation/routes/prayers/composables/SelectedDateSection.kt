package com.example.domes.presentation.routes.prayers.composables

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateBefore
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.outlined.CalendarViewDay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domes.presentation.common.composables.button.SegmentedAction
import com.example.domes.presentation.common.composables.button.SegmentedActionSection
import com.example.domes.presentation.common.composables.listitem.ListItemDivider
import com.example.domes.presentation.common.composables.shape.Shape
import com.example.domes.presentation.common.utility.RoundedPolygonShape
import com.example.domes.presentation.routes.prayers.UIConstants
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarView
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.chrono.HijrahDate

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SelectedDateSection(
    gregorianDate: LocalDate,
    hijriDate: HijrahDate,
    scope: CoroutineScope,
    isLoading: Boolean,
    bottomSheetExpandAction: () -> Unit,
    collapseBottomSheet: () -> Unit,
    onDateChange: (LocalDate) -> Unit,
) {
    val todayDate = remember { LocalDate.now() }
    val selectedDate = remember { mutableStateOf<LocalDate>(todayDate) }
    val yesterdayDate = selectedDate.value.minusDays(1)
    val tomorrowDate = selectedDate.value.plusDays(1)
    val calendarConfig = CalendarConfig(
        yearSelection = true,
        monthSelection = true,
        style = CalendarStyle.MONTH,
    )

    val shapeParam = Shape(
        sides = 4,
        innerRadius = .352f,
        roundness = .32f,
        rotation = 45f,
        shapeId = Shape.ShapeId.Star
    )
    val shape = remember {
        shapeParam.genShape()
    }
    val clip = remember(shape) {
        RoundedPolygonShape(polygon = shape)
    }

    fun updateDate(date: LocalDate) {
        selectedDate.value = date
        onDateChange(date)
    }

    val pagerState = rememberPagerState(initialPage = 1) { 3 }

    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.settledPage == 0) {
            updateDate(yesterdayDate)
        } else if (pagerState.settledPage == 2) {
            updateDate(tomorrowDate)
        }

        scope.launch {
            pagerState.scrollToPage(1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(UIConstants.SHEET_PEEK_HEIGHT)
            .clickable {
                bottomSheetExpandAction()
            },
        contentAlignment = Alignment.Center,
    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 26.dp, end = 10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            HorizontalPager(
//                pagerState,
//                pageSpacing = 16.dp,
//                modifier = Modifier.fillMaxWidth()
//            ) { index ->
//                when (index) {
//                    1 -> DateSection(gregorianDate, hijriDate)
//                    0 -> DateSection(yesterdayDate)
//                    else -> DateSection(tomorrowDate)
//                }
//            }

        ListItem(
            modifier = Modifier
                .fillMaxWidth(),
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
            headlineContent = {
                Text(
                    text = gregorianDate.format(getFormatter("MMMM yyyy")),
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            leadingContent = {
                Box(
                    modifier = Modifier
                        .clip(clip)
                        .size(52.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = gregorianDate.dayOfWeek.name.take(3),
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = (7).sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                        Text(
                            text = "${gregorianDate.dayOfMonth}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                    }
                }
            },
            supportingContent = {
                Text(
                    text = getFormatter("MMMM d yyyy").format(hijriDate),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            trailingContent = {
                SegmentedActionSection(
                    children = listOf(
                        SegmentedAction(
                            icon = IconSource(
                                Icons.AutoMirrored.Outlined.NavigateBefore, "Yesterday"
                            ), enabled = !isLoading
                        ) { updateDate(yesterdayDate) }, SegmentedAction(
                            icon = IconSource(Icons.Outlined.CalendarViewDay, "Today"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = selectedDate.value != todayDate
                        ) { updateDate(todayDate) }, SegmentedAction(
                            icon = IconSource(
                                Icons.AutoMirrored.Outlined.NavigateNext,
                                "Tomorrow"
                            ),
                            enabled = !isLoading
                        ) { updateDate(tomorrowDate) }
                    )
                )
            }
        )
    }

    ListItemDivider()

    Box(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            .animateContentSize(),
    ) {
        CalendarView(
            useCaseState = rememberUseCaseState(
                visible = true,
                embedded = true,
                onCloseRequest = {
                    collapseBottomSheet()
                },
            ),
            config = calendarConfig,
            selection = CalendarSelection.Date(
                withButtonView = false,
                selectedDate = selectedDate.value,
            ) { newDate ->
                selectedDate.value = newDate
                onDateChange(selectedDate.value)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedDateSectionPreview() {
    SelectedDateSection(
        gregorianDate = LocalDate.now(),
        hijriDate = HijrahDate.now(),
        scope = rememberCoroutineScope(),
        isLoading = false,
        bottomSheetExpandAction = {},
        collapseBottomSheet = {},
    ) {
        println(it)
    }
}