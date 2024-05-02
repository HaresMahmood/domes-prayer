package com.example.domes.presentation.routes.prayers.composables

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMissedOutgoing
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domes.domain.model.prayer.MissedReason
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerRecordDetail
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.card.ExpandableCard
import com.example.domes.presentation.common.composables.shape.Circle
import com.example.domes.presentation.common.composables.shape.Shape
import com.example.domes.presentation.common.utility.RoundedPolygonShape
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.LocalDateTime

// TODO: Rewrite to be more compact
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerCard(
    prayerTime: PrayerTime,
    prayerRecordDetail: PrayerRecordDetail?,
    navController: NavController,
    isExpanded: Boolean = false,
    isCurrent: Boolean = false,
    hasPassed: Boolean = false,
    isSilent: Boolean = false,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    isBeforeExpandedItem: Boolean = false,
    isAfterExpandedItem: Boolean = false,
    remainingTimePercentage: Float,
    expand: (Boolean) -> Unit,
    onPrayerRecordDetailValueChange: (prayed: Boolean?, notes: String?, missedReasons: MissedReason?) -> Unit = { b: Boolean?, s: String?, mr: MissedReason? -> }
) {
    ExpandableCard(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 0.dp,
//                        end = if (isCurrent) 0.dp else 12.dp,
//                        top = if (isFirstItem || isAfterExpandedItem) 16.dp else 0.dp,
//                        bottom = if (isLastItem || isBeforeExpandedItem) 16.dp else 0.dp,
                    )
                    .alpha(if (hasPassed && !isCurrent && !isExpanded) 0.5f else 1f)
                    .clickable {
                        if (prayerTime.prayer.displayable) {
                            expand(isExpanded)
                        }
                    },
                leadingContent = {
                    Box(contentAlignment = Alignment.Center) {
                        if (isCurrent) {
                            val remainingTimeAnimatedProgress by animateFloatAsState(
                                targetValue = remainingTimePercentage,
                                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                                label = "prayer-progress"
                            )

                            CircularProgressIndicator(
                                progress = remainingTimeAnimatedProgress,
                                modifier = Modifier
                                    .size(40.dp),
                                color = if (remainingTimePercentage < 0.15f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                strokeCap  = StrokeCap.Round,
                            )
                        }
                        IconButton(
                            onClick = {
                                prayerRecordDetail?.let { onPrayerRecordDetailValueChange(!it.prayed, null, null) }
                            }
                        ) {
                            if (prayerTime.prayer.displayable && prayerRecordDetail != null) {
                                if (prayerRecordDetail.prayed) {
                                    Icon(Icons.Outlined.CheckCircle, "Prayed")
                                } else {
                                    Icon(Icons.Outlined.RadioButtonUnchecked, "Not prayed")
                                }
                            } else {
                                Circle(color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        val shapeParam = Shape(
                            sides = 4,
                            innerRadius = .352f,
                            roundness = .32f,
                            rotation = 45f,
                            shapeId = Shape.ShapeId.Star
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
                            Icon(
                                painterResource(prayerTime.prayer.iconResId),
                                prayerTime.prayer.name,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    } else {
                        val shapeParam = Shape(
                            roundness = .4f,
                            sides = 4,
                            rotation = 90f,
                            shapeId = Shape.ShapeId.Polygon
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
                            Icon(
                                painterResource(prayerTime.prayer.iconResId),
                                prayerTime.prayer.name
                            )
                        }
                    }
                },
                supportingContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
        },
        expandedContent = {
            // TODO: Is this bad?
            val isDropDownExpanded = remember { mutableStateOf(false) }

            Divider()
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (hasPassed && prayerRecordDetail?.prayed == false) {
                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = isDropDownExpanded.value,
                        onExpandedChange = {
                            isDropDownExpanded.value = it
                        }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            value = prayerRecordDetail.missedReason?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            label = { Text("Missed due to") },
                            leadingIcon = {
                                prayerRecordDetail.missedReason?.iconResId?.let { id ->
                                    Icon(
                                        painterResource(id = id),
                                        "Missed reason"
                                    )
                                } ?: Icon (
                                    Icons.AutoMirrored.Filled.CallMissedOutgoing,
                                    contentDescription = "Missed Reason"
                                )
                            },
                            trailingIcon = { IconButton(onClick = { isDropDownExpanded.value = !isDropDownExpanded.value }) {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropDownExpanded.value)
                            } },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = isDropDownExpanded.value,
                            onDismissRequest = {
                                isDropDownExpanded.value = !isDropDownExpanded.value
                            },
                        ) {
                            MissedReason.ALL.forEach {
                                DropdownMenuItem(
                                    text = { Text(it?.name ?: "None") },
                                    leadingIcon = {
                                        it?.iconResId?.let { id ->
                                            Icon(
                                                painterResource(id),
                                                it.name
                                            )
                                        }
                                    },
                                    onClick = {
                                        onPrayerRecordDetailValueChange(null, null, it)
                                        isDropDownExpanded.value = !isDropDownExpanded.value
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = prayerRecordDetail?.notes ?: "",
                    onValueChange = {
                        onPrayerRecordDetailValueChange(null, it, null) // FIXME: Probably very inefficient!!! updating database on change
                    },
                    leadingIcon = { Icon(Icons.AutoMirrored.Outlined.TextSnippet, null) },
                    label = { Text("Notes") },
                    placeholder = { Text("Reason missed • Raka'ats prayed • Du'as read") },
                    maxLines = 2,
                )
            }
            Divider()
            LazyRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                item {
                    ActionSectionChip("Snooze alert", icon = IconSource(Icons.Outlined.Snooze))
                }
                item {
                    ActionSectionChip(
                        "Manage notifications",
                        icon = IconSource(Icons.Outlined.Notifications),
                        onClick = {
                            navController.navigate("${Screen.ManagePrayer.route}?prayer=${prayerTime.prayer.name}")
                        }
                    )
                }
                item {
                    ActionSectionChip("Snooze alert", icon = IconSource(Icons.Outlined.Snooze))
                }
            }
        },
        expand = expand,
    )
}


//@Preview(
//    showBackground = true,
//    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
//    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
//)
//@Composable
//fun Preview() {
//    // fake navController for preview
//    val navController = remember {
//        NavController(LocalContext.current)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(
//                MaterialTheme.colorScheme.surfaceColorAtElevation(
//                    1.dp
//                ), RoundedCornerShape(35.dp)
//            ),
//        verticalArrangement = Arrangement.spacedBy(2.dp),
//    ) {
//        PrayerCard(
//            prayerTime = PrayerTime(
//                Prayer.FAJR,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(10)
//            ),
//            prayerRecordDetail = PrayerRecordDetail(Prayer.FAJR, false),
//            remainingTimePercentage = 0.5f,
//            isExpanded = true,
//            hasPassed = true,
//            expand = {},
//            navController = navController
//        )
//
//        PrayerCard(
//            prayerTime = PrayerTime(
//                Prayer.DHUHR,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(10)
//            ),
//            prayerRecordDetail = PrayerRecordDetail(Prayer.DHUHR, false),
//            isCurrent = true,
//            remainingTimePercentage = 0.5f,
//            isExpanded = false,
//            expand = {},
//            navController = navController
//        )
//    }
//}
