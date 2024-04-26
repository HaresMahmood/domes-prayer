package com.example.domes.presentation.routes.settings.routes.notifications.routes.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CompareArrows
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.MarkChatUnread
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.common.composables.button.SegmentedButtonItem
import com.example.domes.presentation.common.composables.button.SegmentedButtons

@Composable
fun ManagePrayerCard(
    isDefaultNotification: Boolean = false,
    isExpanded: Boolean = false,
    onExpand: (Boolean) -> Unit,
) {
    val backgroundElevation by animateDpAsState(
        targetValue = if (isExpanded) 5.dp else 1.dp
    )
    val cornerSize by animateDpAsState(
        targetValue = if (isExpanded) 12.dp else 35.dp // TODO: Use material shapes
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(backgroundElevation),
                RoundedCornerShape(cornerSize)
            )
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 5.dp,
                    end = 5.dp,
                    bottom = if (isExpanded) 0.dp else 8.dp
                )
                .clickable {
                    onExpand(isExpanded)
                },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
            leadingContent = {
                Icon(
                    Icons.Outlined.Timer,
                    "Setting icon",
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            headlineContent = { Text("Time") },
            supportingContent = {
                Text(
                    "3 mins",
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
        )
        if (isExpanded) {
            if (!isDefaultNotification) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp,
                            start = 5.dp,
                            end = 5.dp,
                        ),
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                    ),
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Outlined.CompareArrows,
                            "Setting icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    headlineContent = {
                        SegmentedButtons {
                            SegmentedButtonItem(
                                selected = false,
                                onClick = { },
                                label = { Text("Before") },
                                icon = { Icon(Icons.AutoMirrored.Outlined.Undo, null) },
                            )
                            SegmentedButtonItem(
                                selected = true,
                                onClick = { },
                                label = { Text("After") },
                                icon = { Icon(Icons.AutoMirrored.Outlined.Redo, null) },
                            )
                        }
                    },
                )
            }
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 5.dp,
                        end = 5.dp,
                    ),
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                ),
                leadingContent = {
                    Icon(
                        Icons.Outlined.MarkChatUnread,
                        "Setting icon",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                headlineContent = { Text("Alert type") },
                supportingContent = {
                    Text(
                        "Sound",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
            )
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 8.dp,
                    )
                    .clickable {

                    },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                ),
                leadingContent = {
                    Icon(
                        Icons.Outlined.GraphicEq,
                        "Setting icon",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                headlineContent = { Text("Sound") },
                supportingContent = {
                    Text(
                        "Device default",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
            )
        }
    }
}