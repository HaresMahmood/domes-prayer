package com.example.domes.presentation.common.composables.button

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateBefore
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

data class SegmentedAction(
    val label: String? = null,
    val icon: IconSource? = null,
    val colors: IconButtonColors? = null,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)

@Composable
fun SegmentedActionSection(
    children: List<SegmentedAction?> = emptyList(),
) {
    @Composable
    fun corners(pos: Int): Shape {
        return when (pos) {
            0 -> MaterialTheme.shapes.extraLarge.copy(
                topEnd = MaterialTheme.shapes.extraSmall.topEnd,
                bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
            )

            children.size - 1 -> MaterialTheme.shapes.extraLarge.copy(
                topStart = MaterialTheme.shapes.extraSmall.topStart,
                bottomStart = MaterialTheme.shapes.extraSmall.bottomStart
            )

            else -> MaterialTheme.shapes.extraSmall
        }
    }

    Row(
        modifier = Modifier.animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        children.forEachIndexed { index, child ->
            child?.let {
                FilledTonalIconButton(
                    modifier = Modifier.then(
                        Modifier
                            .size(32.dp)
                    ),
                    enabled = child.enabled,
                    onClick = child.onClick,
                    shape = corners(index),
                    colors = child.colors ?: IconButtonDefaults.filledTonalIconButtonColors()
                ) {
                    child.icon?.let {
                        IconComponent(modifier = Modifier.size(22.dp), iconSource = it)
                    }

                    child.label?.let {
                        Text(text = it, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SegmentedButtonPreview() {
    SegmentedActionSection(
        listOf(
            SegmentedAction(
                onClick = {},
                icon = IconSource(
                    Icons.AutoMirrored.Outlined.NavigateBefore,
                    contentDescription = "Yesterday"
                ),
            ),
            SegmentedAction(
                onClick = {},
                label = "1"
            ),
            SegmentedAction(
                onClick = {},
                icon = IconSource(
                    Icons.AutoMirrored.Outlined.NavigateNext,
                    contentDescription = "Tomorrow"
                )
            )
        )
    )
}