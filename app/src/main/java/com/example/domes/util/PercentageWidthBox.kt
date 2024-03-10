package com.example.domes.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    children: List<PercentageWidthBoxChild>
) {
    Row(modifier = Modifier.fillMaxWidth().height(20.dp)) {
        children.forEach { child ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(child.widthFraction)
                    .height(child.parentContainerHeight)
                    .weight(1f, fill = false),
                contentAlignment = Alignment.CenterStart,
                content = child.composable
            )
        }
    }
}
