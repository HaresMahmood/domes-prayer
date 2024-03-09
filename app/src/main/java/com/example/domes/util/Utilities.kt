package com.example.domes.util

import android.graphics.PointF
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.plus
import androidx.core.graphics.times
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.MutableCubic
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.TransformResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

internal fun Float.toRadians() = this * PI.toFloat() / 180f

internal fun Offset.rotate90() = Offset(-y, x)

internal fun directionVector(angleRadians: Float) = Offset(cos(angleRadians), sin(angleRadians))

internal fun Offset.rotate(angleRadians: Float): Offset {
    val vec = directionVector(angleRadians)
    return vec * x + vec.rotate90() * y
}

internal val PointZero = PointF(0f, 0f)

internal fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero
) = directionVectorPointF(angleRadians) * radius + center


internal fun directionVectorPointF(angleRadians: Float) =
    PointF(cos(angleRadians), sin(angleRadians))

@Composable
fun PolygonComposable(polygon: RoundedPolygon, modifier: Modifier = Modifier) =
    PolygonComposableImpl(polygon, modifier)

@Composable
internal fun PolygonComposableImpl(
    polygon: RoundedPolygon,
    modifier: Modifier = Modifier,
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

class RoundedPolygonShape(
    private val polygon: RoundedPolygon
) : Shape {
    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = polygon.cubics.toPath()
        // below assumes that you haven't changed the default radius of 1f, nor the centerX and centerY of 0f
        // By default this stretches the path to the size of the container, if you don't want stretching, use the same size.width for both x and y.
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)
        path.transform(matrix)

        return Outline.Generic(path)
    }
}

/**
 * Utility functions providing more idiomatic ways of transforming RoundedPolygons and
 * transforming shapes into a compose Path, for drawing them.
 *
 * This should in the future move into the compose library, maybe with additional API that makes
 * it easier to create, draw, and animate from Compose apps.
 *
 * This code is just here for now prior to integration into compose
 */

/**
 * Scales a shape (given as a Sequence) in place.
 * As this works in Sequences, it doesn't create the whole list at any point, only one
 * MutableCubic is (re)used.
 */
fun Sequence<MutableCubic>.scaled(scale: Float) = map {
    it.transform { x, y -> TransformResult(x * scale, y * scale) }
    it
}

/**
 * Scales a shape (given as a List), creating a new List.
 */
fun List<Cubic>.scaled(scale: Float) = map {
    it.transformed { x, y -> TransformResult(x * scale, y * scale) }
}

/**
 * Transforms a [RoundedPolygon] with the given [Matrix]
 */
fun RoundedPolygon.transformed(matrix: Matrix): RoundedPolygon =
    transformed { x, y ->
        val transformedPoint = matrix.map(Offset(x, y))
        TransformResult(transformedPoint.x, transformedPoint.y)
    }

/**
 * Calculates and returns the bounds of this [RoundedPolygon] as a [Rect]
 */
fun RoundedPolygon.getBounds() = calculateBounds().let { Rect(it[0], it[1], it[2], it[3]) }

/**
 * Function used to create a Path from a list of Cubics.
 */
fun List<Cubic>.toPath(path: Path = Path()): Path {
    path.rewind()
    firstOrNull()?.let { first ->
        path.moveTo(first.anchor0X, first.anchor0Y)
    }
    for (bezier in this) {
        path.cubicTo(
            bezier.control0X, bezier.control0Y,
            bezier.control1X, bezier.control1Y,
            bezier.anchor1X, bezier.anchor1Y
        )
    }
    path.close()
    return path
}

/**
 * Transforms the morph at a given progress into a [Path].
 * It can optionally be scaled, using the origin (0,0) as pivot point.
 */
fun Morph.toAndroidPath(progress: Float, scale: Float = 1f,
                        path: android.graphics.Path = android.graphics.Path()):
        android.graphics.Path {
    var first = true
    path.rewind()
    forEachCubic(progress) { bezier ->
        if (first) {
            path.moveTo(bezier.anchor0X * scale, bezier.anchor0Y * scale)
            first = false
        }
        path.cubicTo(
            bezier.control0X * scale, bezier.control0Y * scale,
            bezier.control1X * scale, bezier.control1Y * scale,
            bezier.anchor1X * scale, bezier.anchor1Y * scale
        )
    }
    path.close()
    return path
}

internal const val DEBUG = false

internal inline fun debugLog(message: String) {
    if (DEBUG) {
        println(message)
    }
}