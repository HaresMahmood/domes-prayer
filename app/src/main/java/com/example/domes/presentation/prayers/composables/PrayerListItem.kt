package com.example.domes.presentation.prayers.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.util.RoundedPolygonShape
import com.example.domes.presentation.util.ShapeParameters

@Composable
fun PrayerListItem(
    prayerName: String,
    icon: Any,
    prayerTime: String,
    onClick: () -> Unit = {},
    isSilent: Boolean = false,
    isPassed: Boolean = false,
    isActive: Boolean = false,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
//            .offset(if (isActive) (-16).dp else 0.dp)
            .padding(
                start = 0.dp,
//                end = if (isActive) 0.dp else 12.dp,
                top = if (isFirstItem) 16.dp else 0.dp,
                bottom = if (isLastItem) 16.dp else 0.dp,
            )
            .alpha(if (isPassed) 0.5f else 1f),
        leadingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                if (isSilent) {
                    // Icon(
                    //     Icons.Outlined.NotificationsOff,
                    //     modifier = Modifier.width(18.dp),
                    //     contentDescription = "Silent"
                    // )
                }

                Box(contentAlignment = Alignment.Center) {
                    if (isActive) {
                        CircularProgressIndicator(
                            progress = 0.75f,
                            modifier = Modifier
                                .size(40.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    IconButton(onClick) {
                        Icon(Icons.Outlined.RadioButtonUnchecked, "Done")
                    }
                }
            }
        },
        headlineContent = {
            Text(
                prayerName,
                style = if (isActive) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            if (isActive) {
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
                    when (icon) {
                        is ImageVector -> Icon(icon, prayerName)
                        is Painter -> Icon(icon, prayerName)
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
                    when (icon) {
                        is ImageVector -> Icon(icon, prayerName)
                        is Painter -> Icon(icon, prayerName)
                        else -> throw IllegalArgumentException("Invalid icon type")
                    }
                }
            }
        },
        supportingContent = {
            Text(prayerTime)
        }
    )
}