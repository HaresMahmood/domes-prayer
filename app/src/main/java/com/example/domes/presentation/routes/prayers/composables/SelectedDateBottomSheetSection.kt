package com.example.domes.presentation.routes.prayers.composables

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateBefore
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.outlined.Today
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
import com.example.domes.presentation.common.composables.button.SegmentedAction
import com.example.domes.presentation.common.composables.button.SegmentedActionSection
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
fun SelectedDateBottomSheetSection(
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
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // A circle with the day of the month, and the corresponding weekday name above it
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = gregorianDate.dayOfWeek.name.take(3),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "${gregorianDate.dayOfMonth}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
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

                val selectionActions = mutableListOf<SegmentedAction>().apply {
                    add(
                        SegmentedAction(
                            { updateDate(-1) },
                            icon = IconSource(
                                Icons.AutoMirrored.Outlined.NavigateBefore,
                                "Yesterday"
                            )
                        )
                    )
                    if (selectedDate.value?.equals(todayDate) == false) {
                        add(
                            SegmentedAction(
                                { updateDate(0, true) },
                                icon = IconSource(Icons.Outlined.Today, "Today"),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.inversePrimary,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        )
                    }
                    add(
                        SegmentedAction(
                            { updateDate(1) },
                            icon = IconSource(Icons.AutoMirrored.Outlined.NavigateNext, "Tomorrow")
                        )
                    )
                }

                SegmentedActionSection(children = selectionActions)
            }
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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