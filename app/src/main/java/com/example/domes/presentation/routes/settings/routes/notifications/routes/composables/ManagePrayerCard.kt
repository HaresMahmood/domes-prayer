package com.example.domes.presentation.routes.settings.routes.notifications.routes.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.MaterialTheme
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
import com.example.domes.presentation.common.composables.listitem.IndicativeTrailingContent
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
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
            SettingsListItem(
                title = "Time",
                icon = IconSource(Icons.Outlined.Timer),
                value = time.plusMinutes(notification?.offset?.toLong() ?: 0)
                    .format(getFormatter("HH:mm a")),
                trailingContent = {
                    IndicativeTrailingContent {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                isLastItem = true,
            )
        },
        expandedContent = {
            SettingsListItem(
                title = "Offset",
                icon = IconSource(Icons.Outlined.Update),
                value = "${notification?.offset?.let { abs(it) }} mins",
                trailingContent = {
                    SegmentedActionSection(listOf(
                        SegmentedAction(
                            icon = IconSource(Icons.Outlined.Remove, "Less"),
                            enabled = notification?.offset != 0
                        ) {
                            notification?.let {
                                changeOffset(-1)
                            }
                        },
                        SegmentedAction(
                            icon = IconSource(Icons.Outlined.Tune, "Custom"),
                        ) {},
                        SegmentedAction(
                            icon = IconSource(Icons.Outlined.Add, "More")
                        ) {
                            notification?.let {
                                changeOffset(1)
                            }
                        }
                    ))
                },
                isLastItem = true
            )
            SettingsListItem(
                icon = IconSource(Icons.AutoMirrored.Outlined.CompareArrows),
                trailingContent = {
                    SegmentedButtons {
                        OffsetPolarity.entries.forEach { offsetRelation ->
                            val selected = offsetRelation.index == notification?.offset?.sign
                            SegmentedButtonItem(
                                selected = selected,
                                enabled = notification?.offset != 0,
                                onClick = {
                                    if (!selected) {
                                        notification?.let { _ ->
                                            changeOffsetRelation()
                                        }
                                    }
                                },
                                label = {
                                    Text(
                                        offsetRelation.name.lowercase()
                                            .replaceFirstChar { it.uppercase() })
                                },
                            )
                        }
                    }
                },
                isLastItem = true
            )
            SettingsListItem(
                title = "Alert type",
                icon = IconSource(Icons.Outlined.MarkChatUnread),
                value = notification?.alertType.toString(),
                isLastItem = true,
            )
            SettingsListItem(
                title = "Sound",
                icon = IconSource(Icons.Outlined.GraphicEq),
                value = "Device default",
                isLastItem = true,
                onClick = {}
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
fun ManagePrayerCard() {
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