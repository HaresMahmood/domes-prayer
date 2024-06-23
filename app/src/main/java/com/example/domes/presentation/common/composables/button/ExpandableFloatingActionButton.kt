package com.example.domes.presentation.common.composables.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

enum class ContainerState {
    Fab,
    Fullscreen,
}

@Composable
fun ExpandableFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: IconSource,
    onExpand: () -> Unit = {},
    onCollapse: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    var containerState by remember { mutableStateOf(ContainerState.Fab) }
    val transition = updateTransition(containerState, label = "container transform")
    val animatedColor by transition.animateColor(
        label = "color",
    ) { state ->
        when (state) {
            ContainerState.Fab -> MaterialTheme.colorScheme.primaryContainer
            ContainerState.Fullscreen -> MaterialTheme.colorScheme.surface
        }
    }

    val cornerRadius by transition.animateDp(
        label = "corner radius",
        transitionSpec = {
            when (targetState) {
                ContainerState.Fab -> tween(
                    durationMillis = 400,
                    easing = EaseOutCubic,
                )

                ContainerState.Fullscreen -> tween(
                    durationMillis = 200,
                    easing = EaseInCubic,
                )
            }
        }
    ) { state ->
        when (state) {
            ContainerState.Fab -> 22.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }
    val elevation by transition.animateDp(
        label = "elevation",
        transitionSpec = {
            when (targetState) {
                ContainerState.Fab -> tween(
                    durationMillis = 400,
                    easing = EaseOutCubic,
                )

                ContainerState.Fullscreen -> tween(
                    durationMillis = 200,
                    easing = EaseOutCubic,
                )
            }
        }
    ) { state ->
        when (state) {
            ContainerState.Fab -> 6.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }
    val padding by transition.animateDp(
        label = "padding",
    ) { state ->
        when (state) {
            ContainerState.Fab -> 16.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }

    transition.AnimatedContent(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(end = padding, bottom = padding)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind { drawRect(animatedColor) },
        transitionSpec = {
            (
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = tween(220, delayMillis = 90)
                            )
                    )
                .togetherWith(fadeOut(animationSpec = tween(90)))
                .using(SizeTransform(clip = false, sizeAnimationSpec = { _, _ ->
                    tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                }))
        }
    ) { state ->
        when (state) {
            ContainerState.Fab -> {
                onCollapse()

                FloatingActionButton(
                    modifier = Modifier,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                    ),
                    onClick = { containerState = ContainerState.Fullscreen }
                ) {
                    IconComponent(modifier = Modifier, iconSource = icon)
                }
            }

            ContainerState.Fullscreen -> {
                onExpand()
                content()
            }
        }
    }
}