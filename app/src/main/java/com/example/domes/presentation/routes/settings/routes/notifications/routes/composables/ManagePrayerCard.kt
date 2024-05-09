package com.example.domes.presentation.routes.settings.routes.notifications.routes.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.MarkChatUnread
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.domes.domain.model.prayer.Notification
import com.example.domes.presentation.common.composables.button.SegmentedAction
import com.example.domes.presentation.common.composables.button.SegmentedActionSection
import com.example.domes.presentation.common.composables.button.SegmentedButtonItem
import com.example.domes.presentation.common.composables.button.SegmentedButtons
import com.example.domes.presentation.common.composables.card.ExpandableCard
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import com.example.domes.presentation.routes.settings.routes.notifications.routes.OffsetPolarity
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.math.sign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePrayerCard(
    time: LocalDateTime,
    notification: Notification? = null,
    isDefaultNotification: Boolean = false,
    isEnabled: Boolean = true,
    isExpanded: Boolean = false,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    isBeforeExpandedItem: Boolean = false,
    isAfterExpandedItem: Boolean = false,
    changeOffset: (Int) -> Unit,
    changeOffsetRelation: () -> Unit,
    expand: (Boolean) -> Unit,
    remove: (() -> Unit)? = null,
) {
    ExpandableCard(
        isEnabled = isEnabled,
        isExpanded = isExpanded,
        isFirstItem = isFirstItem,
        isLastItem = isLastItem,
        isBeforeExpandedItem = isBeforeExpandedItem,
        isAfterExpandedItem = isAfterExpandedItem,
        content = {
            ListItem(
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
                        time.plusMinutes(notification?.offset?.toLong() ?: 0).format(getFormatter("HH:mm a")),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier
                                .height(SwitchDefaults.IconSize * 2) // TODO: This is kinda dumb
                                .width(1.dp)
                        )
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                }
            )
        },
        expandedContent = {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                ),
                leadingContent = {
                    Icon(
                        Icons.Outlined.Update,
                        "Setting icon",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                headlineContent = { Text("Offset") },
                supportingContent = {
                    Text(
                        "${notification?.offset?.let { abs(it) }} mins",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                trailingContent = {
                    SegmentedActionSection(listOf(
                        SegmentedAction(
                            {
                                if (notification?.offset != 0) {
                                    notification?.let {
                                        changeOffset(-1)
                                    }
                                }
                            },
                            icon = IconSource(Icons.Outlined.Remove, "Less"),
                            enabled = notification?.offset != 0
                        ),
                        SegmentedAction(
                            {},
                            icon = IconSource(Icons.Outlined.Tune, "Custom"),
                        ),
                        SegmentedAction(
                            {
                                notification?.let {
                                    changeOffset(1)
                                }
                            },
                            icon = IconSource(Icons.Outlined.Add, "More")
                        )
                    ))
                }
            )
            ListItem(
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
                        OffsetPolarity.entries.forEach { offsetRelation ->
                            val selected = offsetRelation.index == notification?.offset?.sign
                            SegmentedButtonItem(
                                selected = selected,
                                onClick = {
                                    if (!selected) {
                                        notification?.let { _ ->
                                            changeOffsetRelation()
                                        }
                                    }
                                },
                                label = { Text(offsetRelation.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            )
                        }
                    }
                },
            )
            ListItem(
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
                        notification?.alertType.toString(),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
            )
            ListItem(
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

            if (!isDefaultNotification) {
                Divider()
                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(7.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                    onClick = {
                        if (remove != null) {
                            remove()
                        }
                    }
                ) {
                    Icon(Icons.Outlined.DeleteForever, "Remove")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Remove")
                }

//                ActionSectionChip("Snooze alert", icon = IconSource(Icons.Outlined.Snooze))
            }
        },
        expand = expand,
    )
}

@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun Preview() {
    Column {
        ManagePrayerCard(
            time = LocalDateTime.now(),
            isExpanded = true,
            changeOffset = {},
            changeOffsetRelation = {},
            expand = {}
        )
        ManagePrayerCard(
            time = LocalDateTime.now(),
            isExpanded = false,
            changeOffset = {},
            changeOffsetRelation = {},
            expand = {}
        )
    }
}