package com.example.domes.ui.qibla

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import com.example.domes.util.ShapeParameters
import com.example.domes.util.scaled
import com.example.domes.util.toPath
import kotlin.math.min

@Composable
fun PolygonComposable(polygon: RoundedPolygon, modifier: Modifier = Modifier) =
    PolygonComposableImpl(polygon, modifier)

@Preview
@Composable
private fun CompassShape() {
    val shapeParam = ShapeParameters(
        innerRadius = 0.1f,
        roundness = 0.22f,
        shapeId = ShapeParameters.ShapeId.Triangle
    )
    val shape = shapeParam.genShape().normalized()

    Box(
    ) {
        // draw shape
        PolygonComposable(shape)
    }
}

@Composable
internal fun PolygonComposableImpl(
    polygon: RoundedPolygon,
    modifier: Modifier = Modifier,
    debug: Boolean = false
) {
    val sizedShapes = remember(polygon) { mutableMapOf<Size, List<Cubic>>() }
    Box(
        modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                val scale = min(size.width, size.height)
                val shape = sizedShapes.getOrPut(size) { polygon.cubics.scaled(scale) }

                drawPath(shape.toPath(), Color.White)
            }
    )
}

