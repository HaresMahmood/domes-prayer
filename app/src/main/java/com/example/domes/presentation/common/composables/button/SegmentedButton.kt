package com.example.domes.presentation.common.composables.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent


// TODO: optimise

data class SegmentedButtonValues(
    val selected: Boolean,
    val onClick: () -> Unit,
    val modifier: Modifier = Modifier,
    val enabled: Boolean = true,
    val label: @Composable (() -> Unit)? = null,
    val icon: IconSource? = null,
)

@Composable
fun SegmentedButton(
    modifier: Modifier = Modifier,
    values: SegmentedButtonValues,
    enabled: Boolean = true,
) {
    val colors: SegmentedButtonColors = SegmentedButtonsDefaults.colors()
    val textStyle: TextStyle = MaterialTheme.typography.labelLarge

    val styledLabel: @Composable (() -> Unit)? = values.label?.let {
        @Composable {
            val textColor by colors.textColor(selected = values.selected)
            CompositionLocalProvider(LocalContentColor provides textColor) {
                ProvideTextStyle(textStyle, content = values.label)
            }
        }
    }

    val styledIcon: @Composable (() -> Unit)? = values.icon?.let {
        @Composable {
            val iconColor by colors.iconColor(selected = values.selected)
            val clearSemantics = values.label != null && values.selected

            IconComponent(modifier = Modifier
                .requiredSize(ButtonDefaults.IconSize)
                .then(if (clearSemantics) Modifier.clearAndSetSemantics {} else Modifier),
                tint = iconColor,
                iconSource = it)
        }
    }

    val containerColor by animateColorAsState(
        targetValue = if (values.selected) colors.selectedContainerColor else colors.unselectedContainerColor,
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (values.selected) 12.dp else 70.dp,
    )

    Button(
        modifier = values.modifier.then(modifier),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(cornerRadius.value),
        enabled = enabled && values.enabled,
        onClick = values.onClick,
    ) {
        styledIcon?.let {
            it.invoke()
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }

        styledLabel?.invoke()
    }
}

@Composable
fun SegmentedButtons(
    modifier: Modifier = Modifier,
    overflow: Boolean = false,
    listItemChild: Boolean = true,
    enabled: Boolean = true,
    content: List<SegmentedButtonValues> = emptyList(),
) {
    if (!overflow) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (listItemChild) Modifier.padding(start = 60.dp, end = 25.dp) else Modifier
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content.forEach {
                SegmentedButton(
                    values = it, enabled = enabled, modifier = Modifier.weight(1f)
                )
            }
        }
    } else {
        val listState = rememberLazyListState(content.indexOfFirst { it.selected })

        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
            contentPadding = if (listItemChild) PaddingValues(
                start = 60.dp, end = 25.dp
            ) else PaddingValues(0.dp)
        ) {
            items(content.size) {
                SegmentedButton(
                    values = content[it], enabled = enabled
                )
            }
        }
    }
}

object SegmentedButtonsDefaults {
    @Composable
    fun colors(
        selectedTextColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
        selectedIconColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
        unselectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        unselectedIconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedContainerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
        unselectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    ): SegmentedButtonColors = SegmentedButtonColors(
        selectedTextColor = selectedTextColor,
        selectedIconColor = selectedIconColor,
        unselectedTextColor = unselectedTextColor,
        unselectedIconColor = unselectedIconColor,
        selectedContainerColor = selectedContainerColor,
        unselectedContainerColor = unselectedContainerColor,
    )

    internal const val ITEM_ANIMATION_MILLIS: Int = 100
}

@Stable
data class SegmentedButtonColors internal constructor(
    val selectedTextColor: Color,
    val selectedIconColor: Color,
    val unselectedTextColor: Color,
    val unselectedIconColor: Color,
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
) {
    @Composable
    internal fun textColor(selected: Boolean): State<Color> {
        val targetValue = when {
            selected -> selectedTextColor
            else -> unselectedTextColor
        }
        return animateColorAsState(
            targetValue = targetValue, label = "SegmentedButtonsTextColor"
        )
    }

    @Composable
    fun iconColor(selected: Boolean): State<Color> {
        val targetValue = when {
            selected -> selectedIconColor
            else -> unselectedIconColor
        }
        return animateColorAsState(
            targetValue = targetValue, label = "SegmentedButtonsIconColor"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedButtonsPreview() {
    Box(modifier = Modifier.width(400.dp)) {
        SegmentedButtons(
            content = listOf(
                SegmentedButtonValues(
                    selected = true,
                    onClick = {},
                    label = { Text("Selected") },
                ), SegmentedButtonValues(
                    selected = false,
                    onClick = {},
                    label = { Text("Unselected") },
                ), SegmentedButtonValues(
                    selected = false,
                    onClick = {},
                    label = { Text("Unselected") },
                )
            )
        )
    }
}