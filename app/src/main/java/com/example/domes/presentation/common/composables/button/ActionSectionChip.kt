package com.example.domes.presentation.common.composables.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

enum class Variant {
    DEFAULT,
    VISUAL,
    SELECTED,
    ERROR
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionSectionChip(
    title: String? = null,
    variant: Variant = Variant.DEFAULT,
    icon: IconSource? = null,
    trailingIcon: IconSource? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val cornerShapes = when (variant) {
        Variant.DEFAULT, Variant.ERROR -> AssistChipDefaults.shape
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
        Variant.ERROR -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            labelColor = MaterialTheme.colorScheme.onErrorContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.error,
            trailingIconContentColor = MaterialTheme.colorScheme.error
        )
    }

    Box {
        AssistChip(
            modifier = modifier.then(
                if (title == null) Modifier.width(AssistChipDefaults.Height) else Modifier
            ),
            onClick = onClick,
            shape = cornerShapes,
            border = null, // Use AssistChipDefaults.assistChipBorder() if needed based on variant
            leadingIcon = {
                icon?.let { IconComponent(modifier = Modifier.size(AssistChipDefaults.IconSize), iconSource = it) }
            },
            label = {
                title?.let { Text(it) }
            },
            trailingIcon = {
                trailingIcon?.let {
                    IconComponent(modifier = Modifier, iconSource = it)
                }
            },
            colors = colors,
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ActionSectionChipPreview() {
    ActionSectionChip(
//        title = "Test",
        icon = IconSource(Icons.Outlined.PinDrop),
//        variant = Variant.SELECTED,
    )
}