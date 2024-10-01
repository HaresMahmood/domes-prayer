package com.example.domes.presentation.common.composables.button

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.fade
import com.google.accompanist.placeholder.material3.placeholder
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

enum class Color {
    SECONDARY,
    TERTIARY,
    PRIMARY,
    ERROR
}

enum class Shape {
    BUBBLE,
    ROUNDED,
    SQUARE
}

@Composable
fun ActionSectionChip(
    title: String? = null,
    color: Color = Color.SECONDARY,
    shape: Shape = Shape.SQUARE,
    icon: IconSource? = null,
    trailingIcon: IconSource? = null,
    placeholderText: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val cornerShapes = when (shape) {
        Shape.SQUARE -> AssistChipDefaults.shape
        Shape.BUBBLE -> RoundedCornerShape(
            topStartPercent = 12,
            topEndPercent = 45,
            bottomStartPercent = 45,
            bottomEndPercent = 45
        )
        Shape.ROUNDED -> RoundedCornerShape(100)
    }

    val colors = when (color) {
        Color.PRIMARY -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primary,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            leadingIconContentColor = MaterialTheme.colorScheme.inversePrimary,
            trailingIconContentColor = MaterialTheme.colorScheme.inversePrimary
        )
        Color.SECONDARY -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.secondary
        )
        Color.TERTIARY -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.tertiary
        )
        Color.ERROR -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            labelColor = MaterialTheme.colorScheme.onErrorContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.error,
            trailingIconContentColor = MaterialTheme.colorScheme.error
        )
    }

    AssistChip(
        modifier = modifier
            .animateContentSize()
            .then(
                if (title == null) Modifier.width(AssistChipDefaults.Height) else Modifier
            ),
        onClick = onClick,
        shape = cornerShapes,
        border = null, // TODO: Use AssistChipDefaults.assistChipBorder() if needed based on variant
        leadingIcon = {
            icon?.let {
                IconComponent(
                    modifier = Modifier.requiredSize(AssistChipDefaults.IconSize),
                    iconSource = it
                )
            }
        },
        label = {
            title?.let { text ->
                Text(
                    modifier = Modifier.placeholder(
                        visible = placeholderText != null,
                        highlight = PlaceholderHighlight.fade(),
                        shape = RoundedCornerShape(100),
                    ),
                    text = placeholderText ?: text
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                IconComponent(modifier = Modifier, iconSource = it)
            }
        },
        colors = colors,
    )

}

@Preview(
    showBackground = true
)
@Composable
fun ActionSectionChipPreview() {
    ActionSectionChip(
//        title = "Test",
//        placeholderText = "Testing",
        icon = IconSource(Icons.Outlined.PinDrop),
//        variant = Variant.SELECTED,
    )
}