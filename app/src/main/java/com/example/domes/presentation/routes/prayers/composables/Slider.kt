package com.example.domes.presentation.routes.prayers.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.presentation.common.composables.shape.Polygon
import com.example.domes.presentation.common.composables.shape.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Slider(
    value: Float,
    modifier: Modifier = Modifier,
    prayerTimes: List<PrayerTime> = emptyList(),
    currentPrayer: PrayerTime? = null,
    range: ClosedFloatingPointRange<Float> = 0.0f..1f,
) {
    if (value !in range) {
        throw IllegalArgumentException("Value must be within the range $range")
    }

    val shapeParam = Shape(
        roundness = .4f, sides = 4, rotation = 90f, shapeId = Shape.ShapeId.Polygon
    )
    val shape = shapeParam.genShape().normalized()

//    val sliderState = SliderState(value = value, valueRange = range, steps = 5)
//
//    androidx.compose.material3.Slider(
//        state = sliderState,
//        enabled = false,
//        colors = SliderDefaults.colors(
//            disabledThumbColor = MaterialTheme.colorScheme.primary,
//            disabledActiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
//            disabledInactiveTrackColor = MaterialTheme.colorScheme.primary
//        )
//    )

    Box {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (value > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(value)
                            .height(20.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                RoundedCornerShape(
                                    topStartPercent = 100,
                                    bottomStartPercent = 100
                                ).copy(
                                    topEnd = MaterialTheme.shapes.extraSmall.topStart,
                                    bottomEnd = MaterialTheme.shapes.extraSmall.bottomStart
                                )
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(32.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(100)
                        ),
                )
                if (value < 1f) {
                    Box(
                        modifier = Modifier
                            .weight((1f - value))
                            .height(20.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(
                                    topEndPercent = 100,
                                    bottomEndPercent = 100
                                ).copy(
                                    topStart = MaterialTheme.shapes.extraSmall.topStart,
                                    bottomStart = MaterialTheme.shapes.extraSmall.bottomStart
                                )
                            ),
                    )
                }
            }
        }

        PercentageWidthBox(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
            children = prayerTimes.filter { it.prayer.displayable }
                .map { prayerTime ->
                    PercentageWidthBoxChild(
                        composable = {
                            Box(modifier = Modifier.size(10.dp)) {
                                Polygon(
                                    shape,
                                    color = if (prayerTime == currentPrayer) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSecondary,
                                    alpha = if (prayerTime == currentPrayer) 1f else 0.75f
                                )
                            }
                        }, widthFraction = prayerTime.dayFraction?.toFloat()
                    )
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DisplaySliderPreview() {
    Slider(0.5f)
}