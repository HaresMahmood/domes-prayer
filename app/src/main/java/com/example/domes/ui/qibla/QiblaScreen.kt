package com.example.domes.ui.qibla

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.domes.ui.composables.Polygon
import com.example.domes.util.ShapeParameters

@Preview
@Composable
private fun CompassShape() {
    val shapeParam = ShapeParameters(
        innerRadius = 0.1f,
        roundness = 0.22f,
        shapeId = ShapeParameters.ShapeId.Triangle
    )
    val shape = shapeParam.genShape().normalized()

    Box {
        // draw shape
        Polygon(shape)
    }
}