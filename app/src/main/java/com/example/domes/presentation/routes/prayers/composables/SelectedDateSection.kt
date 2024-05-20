package com.example.domes.presentation.routes.prayers.composables

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateBefore
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.outlined.CalendarViewDay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domes.presentation.common.composables.button.SegmentedAction
import com.example.domes.presentation.common.composables.button.SegmentedActionSection
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
import java.time.LocalDate
import java.time.chrono.HijrahDate

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedDateSection(
    gregorianDate: LocalDate,
    hijriDate: HijrahDate,
    bottomSheetExpandAction: () -> Unit,
    collapseBottomSheet: () -> Unit,
    onDateChange: (LocalDate) -> Unit,
) {
    val todayDate = remember { LocalDate.now() }
    val selectedDate = remember { mutableStateOf<LocalDate?>(todayDate) }
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

    fun updateDate(days: Long, isToday: Boolean = false) {
        selectedDate.value = if (isToday) todayDate else selectedDate.value?.plusDays(days)
        selectedDate.value?.let { onDateChange(it) }
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 26.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(clip)
                        .size(52.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(2.dp)
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

                Column {
                    Text(
                        text = gregorianDate.format(getFormatter("MMMM yyyy")),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = getFormatter("MMMM d yyyy").format(hijriDate),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO: Make more efficient
//                AnimatedVisibility (selectedDate.value != todayDate) {
//                    AssistChip(
//                        modifier = Modifier.padding(0.dp),
//                        onClick = { /*TODO*/ },
//                        label = { Text(todayDate.dayOfMonth.toString(), maxLines = 1, overflow = TextOverflow.Visible) }
//                    )
//                }

                SegmentedActionSection(
                    children = listOf(
                        SegmentedAction(
                            { updateDate(-1) },
                            icon = IconSource(
                                Icons.AutoMirrored.Outlined.NavigateBefore,
                                "Yesterday"
                            )
                        ),
                        SegmentedAction(
                            { updateDate(0, true) },
                            icon = IconSource(Icons.Outlined.CalendarViewDay, "Today"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = selectedDate.value?.equals(todayDate) == false
                        ),
                        SegmentedAction(
                            { updateDate(1) },
                            icon = IconSource(Icons.AutoMirrored.Outlined.NavigateNext, "Tomorrow")
                        )
                    )
                )
            }
        }
    }
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
                onDateChange(selectedDate.value!!)
            },
        )
    }
}

//@Preview
//@Composable
//fun SelectedDateSectionPreview() {
//    val shapeParam = Shape(
//        sides = 4,
//        innerRadius = .352f,
//        roundness = .32f,
//        rotation = 45f,
//        shapeId = Shape.ShapeId.Star
//    )
//    val shape = remember {
//        shapeParam.genShape()
//    }
//    val clip = remember(shape) {
//        RoundedPolygonShape(polygon = shape)
//    }
//
//    Box(
//        modifier = Modifier
//            .clip(clip)
//            .size(52.dp)
//            .background(MaterialTheme.colorScheme.tertiaryContainer),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
////                        verticalArrangement = Arrangement.spacedBy(2.dp)
//        ) {
//            Text(
//                text = "M",
//                style = MaterialTheme.typography.labelMedium,
//                fontSize = 8.sp,
//                color = MaterialTheme.colorScheme.onTertiaryContainer,
//            )
//            Text(
//                text = "20",
//                style = MaterialTheme.typography.labelLarge,
//                color = MaterialTheme.colorScheme.onTertiaryContainer,
//            )
//        }
//    }
//}
//}