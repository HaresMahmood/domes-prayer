package com.example.domes.presentation.routes.qibla

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domes.presentation.common.composables.Shape
import com.example.domes.presentation.common.utility.RoundedPolygonShape
import com.example.domes.presentation.common.utility.angleToCoordinates
import com.example.domes.presentation.common.utility.smoothRotation
import com.example.domes.ui.theme.DomesTheme
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    viewModel: QiblaViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

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
    val fillClip = remember(base) {
        RoundedPolygonShape(polygon = base)
    }
    val needleParameters = Shape(
        innerRadius = 0.1f,
        roundness = 0.22f,
        shapeId = Shape.ShapeId.Triangle
    )
    val needle = remember { needleParameters.genShape() }
    val needleClip = remember(needle) { RoundedPolygonShape(polygon = needle) }

    val qiblaSymbolCoordinates = remember { angleToCoordinates(state.qiblaDirection) }

    val currentRotation = smoothRotation(-state.azimuth)
    val rotation by animateFloatAsState(
        targetValue = currentRotation.value,
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearOutSlowInEasing
        )
    )

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
                        state.locale,
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
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
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
                                Text(
                                    text = "N",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .align(Alignment.TopCenter)
                                )
                                Text(
                                    text = "E",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .rotate(90f)
                                        .padding(15.dp)
                                        .align(Alignment.CenterEnd)
                                )
                                Text(
                                    text = "S",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .rotate(180f)
                                        .padding(15.dp)
                                        .align(Alignment.BottomCenter)
                                )
                                Text(
                                    text = "W",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .rotate(270f)
                                        .padding(15.dp)
                                        .align(Alignment.CenterStart)
                                )
                                Text(
                                    text = "Q",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier
                                        .rotate(state.qiblaDirection)
                                        .padding(15.dp)
                                        .align(qiblaSymbolCoordinates)
                                )

                                Box(
//                                  polygon = base,
//                                  color = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier
                                        .clip(fillClip)
                                        .fillMaxHeight(0.5f)
                                        .align(Alignment.Center)
                                        .aspectRatio(1f, true)
                                        .background(MaterialTheme.colorScheme.tertiary),
                                )
                            }
                        }

                        // Needle
                        Box(
                            modifier = Modifier
                                .clip(needleClip)
                                .height(100.dp)
                                .aspectRatio(1f, true)
                                .background(MaterialTheme.colorScheme.onTertiary),
                        )
                    }
                    Spacer(Modifier.size(25.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Badge(
                            modifier = Modifier.animateContentSize(),
                            containerColor = TooltipDefaults.plainTooltipContainerColor,
                            contentColor = TooltipDefaults.plainTooltipContentColor
                        ) {
                            Text("${abs(currentRotation.value.toInt())}°", textAlign = TextAlign.Center)
                        }

                        Text(
                            text = if (state.isFacingQibla) "You're facing the Qibla!" else "Turn to your right",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
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