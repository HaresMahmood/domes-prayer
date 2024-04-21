package com.example.domes.presentation.common.composables.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.IconSource

enum class Variant {
    DEFAULT,
    VISUAL,
    SELECTED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionSectionChip(
    title: String,
    variant: Variant = Variant.DEFAULT,
    icon: IconSource? = null,
    trailingIcon: IconSource? = null,
    onClick: () -> Unit = {},
) {
    val cornerShapes = when (variant) {
        Variant.DEFAULT -> AssistChipDefaults.shape
        Variant.VISUAL -> RoundedCornerShape(
            topStartPercent = 12,
            topEndPercent = 45,
            bottomStartPercent = 45,
            bottomEndPercent = 45
        )
        Variant.SELECTED -> RoundedCornerShape(100)
    }

    val colors = when (variant) {
        Variant.DEFAULT -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.secondary
        )
        Variant.VISUAL -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.tertiary
        )
        Variant.SELECTED -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primary,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            leadingIconContentColor = MaterialTheme.colorScheme.inversePrimary,
            trailingIconContentColor = MaterialTheme.colorScheme.inversePrimary
        )
    }

    AssistChip(
        onClick = onClick,
        shape = cornerShapes,
        border = null, // if (variant == Variant.DEFAULT) AssistChipDefaults.assistChipBorder() else
        leadingIcon =  { icon?.imageVector?.let {
            Icon(
                it,
                null,
                modifier = Modifier.size(ChipDefaults.LeadingIconSize)
            )
        } },
        label = {
            Text(
                text = title,
//                style = MaterialTheme.typography.labelMedium
            )
        },
        trailingIcon = {
            trailingIcon?.imageVector?.let {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    it,
                    null,
                    modifier = Modifier.size(ChipDefaults.LeadingIconSize)
                )
            }
        },
//        contentPadding = PaddingValues(horizontal = 15.dp),
        colors = colors,
    )
}

@Preview(
    showBackground = true
)
@Composable
fun ActionSectionChipPreview() {
    ActionSectionChip(
        title = "Test",
        icon = IconSource(Icons.Outlined.PinDrop),
//        variant = Variant.SELECTED,
    )
}