package com.example.domes.presentation.common.composables.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OptionDialog(
    title: String,
    options: List<Option>,
    setDialogVisibility: (Boolean) -> Unit,
    icon: IconSource? = null,
    onSelection: (index: Int, option: Option) -> Unit,
) {
    OptionDialog(
        state = rememberUseCaseState(
            visible = true,
            onCloseRequest = { setDialogVisibility(false) },
        ),
        header = Header.Default(title, icon),
        selection = OptionSelection.Single(
            options,
            withButtonView = false,
            onSelectOption = onSelection,
        ),
        config = OptionConfig(mode = DisplayMode.LIST)
    )
}