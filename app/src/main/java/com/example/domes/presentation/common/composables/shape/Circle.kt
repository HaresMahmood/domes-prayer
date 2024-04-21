package com.example.domes.presentation.common.composables.shape

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Circle(
    size: Dp = 10.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = Modifier.size(size)) {
        drawCircle(
            color = color,
            radius = size.toPx() / 2
        )
    }
}

@Preview
@Composable
fun CirclePreview() {
    Circle(
        size = 50.dp, // Custom size
    )
}
