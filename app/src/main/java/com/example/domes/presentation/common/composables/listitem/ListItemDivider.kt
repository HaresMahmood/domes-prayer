package com.example.domes.presentation.common.composables.listitem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class Variant {
    HORIZONTAL, VERTICAL
}

@Composable
fun ListItemDivider(
    variant: Variant = Variant.HORIZONTAL
) {
    Divider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
        modifier = Modifier
            .height(if (variant == Variant.HORIZONTAL) 1.dp else SwitchDefaults.IconSize * 2)
            .then(
                if (variant == Variant.VERTICAL) Modifier.width(1.dp) else Modifier.fillMaxWidth(0.88f)
            )
    )
}