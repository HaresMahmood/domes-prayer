package com.example.domes.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DisplaySlider(
    sliderValue: Float, // The current value of the slider
    onValueChange: (Float) -> Unit, // Function to handle value changes
    text: String, // Text to display in the textbox
    modifier: Modifier = Modifier,
    sliderRange: ClosedFloatingPointRange<Float> = 0f..1f, // The range of the slider
) {
    // Calculate the width of the track based on the slider value and range
    val trackWidth = remember { mutableStateOf(0f) }
    val thumbPosition = remember { mutableStateOf(0f) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Textbox above the thumb
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left track (before thumb)
            if (sliderValue > 0f) {
                Box(
                    modifier = Modifier
                        .weight(sliderValue)
                        .height(20.dp)
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(
                                topStartPercent = 100,
                                topEndPercent = 15,
                                bottomStartPercent = 100,
                                bottomEndPercent = 15
                            )
                        ),
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
            if (sliderValue < 1f) {
                Box(
                    modifier = Modifier
                        .weight(1f - sliderValue)
                        .height(20.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(
                                topStartPercent = 15,
                                topEndPercent = 100,
                                bottomStartPercent = 15,
                                bottomEndPercent = 100
                            )
                        ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplaySliderPreview() {
    // Provide a sample slider value and a no-op for onValueChange
    DisplaySlider(
        sliderValue = 0.5f,
        onValueChange = {},
        text = "Sample Text"
    )
}