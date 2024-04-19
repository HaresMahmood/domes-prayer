package com.example.domes.presentation.common.composables.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.IconSource

enum class Variant {
    DEFAULT,
    VISUAL,
    SELECTED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionSectionButton(
    title: String,
    variant: Variant = Variant.DEFAULT,
    icon: IconSource? = null,
    trailingIcon: IconSource? = null,
    onClick: () -> Unit = {},
) {
    val cornerShapes = when (variant) {
        Variant.DEFAULT -> RoundedCornerShape(100)
        Variant.VISUAL -> RoundedCornerShape(
            topStartPercent = 12,
            topEndPercent = 45,
            bottomStartPercent = 45,
            bottomEndPercent = 45
        )
        Variant.SELECTED -> MaterialTheme.shapes.medium
    }

    val colors = when (variant) {
        Variant.DEFAULT -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        Variant.VISUAL -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        Variant.SELECTED -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    }

    Button(
        onClick = onClick,
        shape = cornerShapes,
        contentPadding = PaddingValues(horizontal = 15.dp),
        colors = colors,
    ) {
        icon?.imageVector?.let {
            Icon(
                it,
                null,
                modifier = Modifier.size(ChipDefaults.LeadingIconSize)
            )
        }
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall
        )

        trailingIcon?.imageVector?.let {
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Icon(
                it,
                null,
                modifier = Modifier.size(ChipDefaults.LeadingIconSize)
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    ActionSectionButton(
        title = "Test",
        icon = IconSource(Icons.Outlined.PinDrop),
        variant = Variant.VISUAL,
    )
}