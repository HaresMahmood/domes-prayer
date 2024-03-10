package com.example.domes.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PercentageWidthBoxChild(
    val composable: @Composable BoxScope.() -> Unit,
    val parentContainerHeight: Dp,
    val widthFraction: Float
)

@Composable
fun PercentageWidthBox(
    modifier: Modifier = Modifier,
    children: List<PercentageWidthBoxChild>
) {
    Box(modifier = modifier.fillMaxWidth()) {
        children.forEach { child ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = child.widthFraction)
                    .offset(x = calculateOffset(child.widthFraction)),
                content = child.composable
            )
        }
    }
}

private fun calculateOffset(widthFraction: Float): Dp {
    return (widthFraction * 100).dp
}