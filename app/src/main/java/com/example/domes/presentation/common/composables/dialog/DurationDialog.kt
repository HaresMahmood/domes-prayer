package com.example.domes.presentation.common.composables.dialog

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationDialog(
    title: String,
    icon: IconSource? = null,
    bottomActions: List<@Composable () -> Unit> = emptyList(),
    setDialogVisibility: (Boolean) -> Unit,
    affirmativeButton: SelectionButton? = null,
    onAffirmative: (result: Bundle) -> Unit,
) {
    val selectedTimeInSeconds = remember { mutableStateOf<Long>(240) }

    val timePickerState = rememberTimePickerState(
        is24Hour = true,
    )

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier =
            Modifier
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column {
                DialogHeader(title, icon)
                Spacer(Modifier.size(32.dp))
                TimePicker(
                    modifier = Modifier.fillMaxWidth(),
                    state = timePickerState,
                )
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
                            bottomActions[index]()
                        }
                    }
                }

                Divider()
                Row(
                    modifier = Modifier
                        .padding(7.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {}) { Text("Cancel") }
                    TextButton(onClick = {}) { Text("Done") }
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