package com.example.domes.presentation.routes.qibla

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Qibla
import com.example.domes.presentation.common.composables.Shape
import com.example.domes.presentation.common.utility.RoundedPolygonShape
import com.example.domes.presentation.common.utility.SmoothRotation
import com.example.domes.presentation.common.utility.angleToCoordinates
import com.example.domes.ui.theme.DomesTheme
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    viewModel: QiblaViewModel = hiltViewModel()
) {
    val baseParameters = Shape(
        sides = 16,
        innerRadius = .892f,
        roundness = 1f,
        shapeId = Shape.ShapeId.Star
    )
    val base = remember {
        baseParameters.genShape()
    }
    val baseClip = remember(base) {
        RoundedPolygonShape(polygon = base)
    }
    val needleParameters = Shape(
        innerRadius = 0.1f,
        roundness = 0.22f,
        shapeId = Shape.ShapeId.Triangle
    )
    val needle = remember { needleParameters.genShape() }
    val needleClip = remember(needle) { RoundedPolygonShape(polygon = needle) }

    val currentRotation = SmoothRotation(-viewModel.azimuth.floatValue)
    val rotation by animateFloatAsState(
        targetValue = currentRotation.value,
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearOutSlowInEasing
        )
    )

    val coordinates = Coordinates(55.825313, -4.293643)
    val qibla = remember { Qibla(coordinates).direction }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = "Help")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(Icons.Outlined.PinDrop, contentDescription = "Location")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        "Glasgow City, UK",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(Icons.Outlined.CompassCalibration, contentDescription = "Calibrate")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Calibrate")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.rotate(rotation)) {
                        Box(
                            modifier = Modifier
                                .clip(baseClip)
                                .fillMaxWidth()
                                .aspectRatio(1f, true)
                                .background(MaterialTheme.colorScheme.tertiaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            // North-east-south-west and Qibla (Q) labels
                            Text(
                                text = "N",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(15.dp)
                                    .align(Alignment.TopCenter)
                            )
                            Text(
                                text = "E",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .rotate(90f)
                                    .padding(15.dp)
                                    .align(Alignment.CenterEnd)
                            )
                            Text(
                                text = "S",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .rotate(180f)
                                    .padding(15.dp)
                                    .align(Alignment.BottomCenter)
                            )
                            Text(
                                text = "W",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .rotate(270f)
                                    .padding(15.dp)
                                    .align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Q",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .rotate(qibla.toFloat())
                                    .padding(15.dp)
                                    .align(angleToCoordinates(qibla.toFloat()))
                            )
                        }
                    }
                    // Needle
                        Box(
                            modifier = Modifier
                                .clip(needleClip)
                                .height(130.dp)
                                .aspectRatio(1f, true)
                                .background(MaterialTheme.colorScheme.tertiary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "${abs(currentRotation.value.toInt())}°",
                                modifier = Modifier.padding(bottom = 15.dp),
                                color = MaterialTheme.colorScheme.onTertiary,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        QiblaScreen()
    }
}