package com.example.domes.presentation.common.composables.dialog

import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.input.models.Input
import com.maxkeppeler.sheets.input.models.InputSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InputDialog(
    title: String,
    inputs: List<Input>,
    setDialogVisibility: (Boolean) -> Unit,
    icon: IconSource? = null,
    affirmativeButton: SelectionButton? = null,
    onAffirmative: (result: Bundle) -> Unit,
) {
    com.maxkeppeler.sheets.input.InputDialog(
        state = rememberUseCaseState(
            visible = true,
            onCloseRequest = { setDialogVisibility(false) },
        ),
        header = Header.Default(title, icon),
        selection = InputSelection(
            inputs,
            positiveButton = affirmativeButton ?: SelectionButton("Done", IconSource(Icons.Outlined.Done), ButtonStyle.FILLED),
            negativeButton = SelectionButton("Cancel", IconSource(Icons.Outlined.Close), ButtonStyle.OUTLINED),
            onPositiveClick = onAffirmative,
        ),
    )
}