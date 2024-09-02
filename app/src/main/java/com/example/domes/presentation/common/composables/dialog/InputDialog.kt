package com.example.domes.presentation.common.composables.dialog

import android.os.Bundle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton

//import com.maxkeppeler.sheets.input.models.Input
//import com.maxkeppeler.sheets.input.models.InputSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InputDialog(
    title: String,
    icon: IconSource? = null,
//    inputs: List<Input>,
    setDialogVisibility: (Boolean) -> Unit,
    affirmativeButton: SelectionButton? = null,
    onAffirmative: (result: Bundle) -> Unit,
) {
//    com.maxkeppeler.sheets.input.InputDialog(
//        state = rememberUseCaseState(
//            visible = true,
//            onCloseRequest = { setDialogVisibility(false) },
//        ),
//        header = Header.Custom { DialogHeader(title, icon) },
//        selection = InputSelection(
//            inputs,
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
//            onPositiveClick = onAffirmative,
//        ),
//    )
}