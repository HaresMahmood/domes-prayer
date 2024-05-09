package com.example.domes.presentation.common.composables.listitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IndicativeTrailingContent(
    content: @Composable (RowScope.() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
//        isIndicitive.also {
//
//        }

        Divider(
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .height(SwitchDefaults.IconSize * 2) // TODO: This is kinda dumb
                .width(1.dp)
        )
        content()
    }
}