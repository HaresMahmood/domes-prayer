package com.example.domes.presentation.common.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import com.example.domes.presentation.common.utility.scaled
import com.example.domes.presentation.common.utility.toPath
import kotlin.math.min

@Composable
fun Polygon(polygon: RoundedPolygon, modifier: Modifier = Modifier, color: Color = Color.White, alpha: Float = 1f,) =
    PolygonComposableImpl(polygon, modifier, color, alpha)

@Composable
internal fun PolygonComposableImpl(
    polygon: RoundedPolygon,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    alpha: Float = 1f,
) {
    val sizedShapes = remember(polygon) { mutableMapOf<Size, List<Cubic>>() }
    Box(
        modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                val scale = min(size.width, size.height)
                val shape = sizedShapes.getOrPut(size) { polygon.cubics.scaled(scale) }

                drawPath(shape.toPath(), color, alpha)
            }
    )
}