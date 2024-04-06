package com.example.domes.presentation.prayers.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.presentation.prayers.UIConstants.Companion.getFormatter
import com.example.domes.presentation.prayers.UIConstants.Companion.getFormatter
import com.example.domes.presentation.util.RoundedPolygonShape
import com.example.domes.presentation.util.ShapeParameters

// TODO: Rewrite to be more compact
@Composable
fun PrayerListItem(
    prayerTime: PrayerTime,
    isCurrent: Boolean = false,
    hasPassed: Boolean = false,
    isSilent: Boolean = false,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    calculateProgress: () -> Float,
    calculateProgress: () -> Float,
    onClick: () -> Unit = {},
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 0.dp,
                end = if (isCurrent) 0.dp else 12.dp,
                top = if (isFirstItem) 16.dp else 0.dp,
                bottom = if (isLastItem) 16.dp else 0.dp,
            )
            .alpha(if (hasPassed && !isCurrent) 0.5f else 1f),
        leadingContent = {
                Box(contentAlignment = Alignment.Center) {
                    if (isCurrent) {
                        val prayerTimeAnimatedProgress by animateFloatAsState(
                            targetValue = calculateProgress(),
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                            label = "prayer-progress"
                        )
                        
                        CircularProgressIndicator(
                            progress = prayerTimeAnimatedProgress,
                            modifier = Modifier
                                .size(40.dp),
                            color = if (prayerTimeAnimatedProgress > 0.75f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(onClick) {
                        if (prayerTime.prayer.displayable) {
                            Icon(Icons.Outlined.RadioButtonUnchecked, "Done")
                        }
                        else {
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .aspectRatio(1f)
                                    .background(MaterialTheme.colorScheme.onSurface)
                                    .clip(RoundedCornerShape(100)),
                            )
                        }
                    }
                }
        },
        headlineContent = {
            Text(
                prayerTime.prayer.name,
                style = if (isCurrent) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            if (isCurrent) {
                val shapeParam = ShapeParameters(
                    sides = 4,
                    innerRadius = .352f,
                    roundness = .32f,
                    rotation = 45f,
                    shapeId = ShapeParameters.ShapeId.Star
                )
                val shape = remember {
                    shapeParam.genShape()
                }
                val clip = remember(shape) {
                    RoundedPolygonShape(polygon = shape)
                }

                Box(
                    modifier = Modifier
                        .clip(clip)
                        .size(72.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    when (prayerTime.prayer.icon) {
                        is ImageVector -> Icon(prayerTime.prayer.icon, prayerTime.prayer.name)
//                        is Painter -> Icon(prayerTime.prayer.icon, prayerTime.prayer.name)
                        else -> throw IllegalArgumentException("Invalid icon type")
                    }
                }
            } else {
                val shapeParam = ShapeParameters(
                    roundness = .4f,
                    sides = 4,
                    rotation = 90f,
                    shapeId = ShapeParameters.ShapeId.Polygon
                )
                val shape = remember {
                    shapeParam.genShape()
                }
                val clip = remember(shape) {
                    RoundedPolygonShape(polygon = shape)
                }

                Box(
                    modifier = Modifier
                        .clip(clip)
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    when (prayerTime.prayer.icon) {
                        is ImageVector -> Icon(prayerTime.prayer.icon, prayerTime.prayer.name)
//                        is Painter -> Icon(prayerTime.prayer.icon, prayerTime.prayer.name)
                        else -> throw IllegalArgumentException("Invalid icon type")
                    }
                }
            }
        },
        supportingContent = {
            Row (
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(prayerTime.time.format(getFormatter("HH:mm a")))
                Text(prayerTime.time.format(getFormatter("HH:mm a")))

                if (isSilent) {
                    Icon(
                        Icons.Outlined.NotificationsOff,
                        modifier = Modifier.width(15.dp),
                        contentDescription = "Silent"
                    )
                }
            }
        }
    )
}