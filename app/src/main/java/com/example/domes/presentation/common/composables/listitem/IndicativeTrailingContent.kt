package com.example.domes.presentation.common.composables.listitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun IndicativeTrailingContent(
    content: @Composable (RowScope.() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ListItemDivider(Variant.VERTICAL)
        content()
    }
}