package com.example.domes.presentation.routes.prayers.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

data class PercentageWidthBoxChild(
    val composable: @Composable BoxScope.() -> Unit,
    val parentContainerHeight: Dp? = Dp.Unspecified,
    val widthFraction: Float?
)

@Composable
fun PercentageWidthBox(
    modifier: Modifier = Modifier,
    children: List<PercentageWidthBoxChild>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        children.forEach { child ->
            child.widthFraction?.let { widthFraction ->
                Box(
                    contentAlignment = BiasAlignment(horizontalBias = widthFraction * 2 - 1, verticalBias = 0f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .heightIn(max = child.parentContainerHeight ?: Dp.Unspecified),
                    content = child.composable
                )
            }
        }
    }
}