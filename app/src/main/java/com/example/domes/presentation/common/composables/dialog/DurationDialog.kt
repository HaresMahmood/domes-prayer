package com.example.domes.presentation.common.composables.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.Duration

data class DurationPreset(
    val title: String,
    val duration: Duration,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationDialog(
    title: String,
    icon: IconSource? = null,
    bottomActions: List<DurationPreset> = emptyList(),
    setDialogVisibility: (Boolean) -> Unit,
    onAffirmative: (duration: Duration) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        is24Hour = true,
    )

    Dialog(
        onDismissRequest = {
            setDialogVisibility(false)
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
//                .width(IntrinsicSize.Min)
                .fillMaxWidth(0.8f)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column {
                DialogHeader(title, icon)
                Spacer(Modifier.size(32.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimeInput(
                        state = timePickerState,
                    )
                }

//                DurationView(
//                    useCaseState = rememberUseCaseState(
//                        visible = true,
//                        embedded = true,
//                        onCloseRequest = { }),
//                    selection = DurationSelection(
//                        withButtonView = false,
//                    ) { newTimeInSeconds ->
//                        selectedTimeInSeconds.value = newTimeInSeconds
//                    },
////                    header = Header.Custom { DialogHeader(title, icon) },
//                    config = DurationConfig(
//                        timeFormat = DurationFormat.HH_MM,
//                        minTime = 30,
//                        maxTime = 240,
//                        icons = LibIcons.Outlined,
////                  orientation = LibOrientation.LANDSCAPE
//                    ),
//                )
                Spacer(Modifier.size(16.dp))
                Divider()

                if (bottomActions.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.padding(vertical = 7.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(bottomActions.size) { index ->
                            ActionSectionChip(
                                title = bottomActions[index].title,
                                onClick = {}
                            )
                        }
                    }
                }

                Divider()
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            setDialogVisibility(false)
                        }
                    ) {
//                        Icon(Icons.Outlined.Close, "Cancel")
//                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Cancel")
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = timePickerState.hour != 0 || timePickerState.minute != 0,
                        onClick = {
                            val duration = Duration.ofMinutes(
                                timePickerState.hour * 60L + timePickerState.minute
                            )

                            onAffirmative(duration)
                            setDialogVisibility(false)
                        }) {
                        Icon(Icons.Outlined.Done, "Done")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Done")
                    }
                }
            }
        }
    }

//    com.maxkeppeler.sheets.duration.DurationDialog(
//        state = rememberUseCaseState(visible = true, onCloseRequest = { }),
//        selection = DurationSelection(
//            positiveButton = affirmativeButton ?: SelectionButton(
//                "Done",
//                IconSource(Icons.Outlined.Done),
//                ButtonStyle.FILLED
//            ),
//            negativeButton = SelectionButton(
//                "Cancel",
//                IconSource(Icons.Outlined.Close),
//                ButtonStyle.TEXT
//            ),
//        ) { newTimeInSeconds ->
//            selectedTimeInSeconds.value = newTimeInSeconds
//        },
//        header = Header.Custom { DialogHeader(title, icon) },
//        config = DurationConfig(
//            timeFormat = DurationFormat.MM_SS,
//            minTime = 30,
//            maxTime = 240,
//            icons = LibIcons.Outlined,
////            orientation = LibOrientation.LANDSCAPE
//        ),
//    )
}