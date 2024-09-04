package com.example.domes.presentation.routes.prayers.composables

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CallMissedOutgoing
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import com.example.domes.domain.model.prayer.MissedReason
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerRecordDetail
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.card.ExpandableCard
import com.example.domes.presentation.common.composables.listitem.ListItemDivider
import com.example.domes.presentation.common.composables.listitem.Option
import com.example.domes.presentation.common.composables.listitem.OptionRow
import com.example.domes.presentation.common.composables.shape.Circle
import com.example.domes.presentation.common.composables.shape.Shape
import com.example.domes.presentation.common.utility.RoundedPolygonShape
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.fade
import com.google.accompanist.placeholder.material3.placeholder
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.LocalDateTime

// TODO: Rewrite to be more compact
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerCard(
    prayer: Prayer,
    prayerTime: PrayerTime?,
    prayerRecordDetail: PrayerRecordDetail?,
    navController: NavController,
    isExpanded: Boolean = false,
    isCurrent: Boolean = false,
    hasPassed: Boolean = false,
    isSilent: Boolean = false,
    isNotificationsAllowed: Boolean = true,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    isBeforeExpandedItem: Boolean = false,
    isAfterExpandedItem: Boolean = false,
    remainingTimePercentage: Float,
    expand: (Boolean) -> Unit,
    onPrayerRecordDetailValueChange: (prayed: Boolean?, notes: String?, missedReasons: MissedReason?) -> Unit = { b: Boolean?, s: String?, mr: MissedReason? -> },
) {
    ExpandableCard(
        isExpanded = isExpanded,
        isFirstItem = isFirstItem,
        isLastItem = isLastItem,
        isBeforeExpandedItem = isBeforeExpandedItem,
        isAfterExpandedItem = isAfterExpandedItem,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (hasPassed && !isCurrent && !isExpanded) 0.5f else 1f),
                    leadingContent = {
                        OutlinedIconButton(
                            border = BorderStroke(
                                if (isCurrent) 4.dp else 1.dp,
                                MaterialTheme.colorScheme.outline.copy(
                                    alpha = 0.5f
                                )
                            ),
                            enabled = prayerTime?.prayer?.displayable ?: false,
                            onClick = {
                                prayerRecordDetail?.let {
                                    onPrayerRecordDetailValueChange(
                                        !it.prayed,
                                        null,
                                        null
                                    )
                                }
                            }
                        ) {
                            if (prayerTime?.prayer?.displayable == true && prayerRecordDetail != null) {
                                if (prayerRecordDetail.prayed) {
                                    Icon(Icons.Outlined.CheckCircle, "Prayed")
                                } else {
                                    Icon(Icons.Outlined.RadioButtonUnchecked, "Not prayed")
                                }
                            } else {
                                Circle(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

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
                                    strokeCap = StrokeCap.Round,
                                    strokeWidth = 4.dp
                                )
                            }
                        }
                    },
                    headlineContent = {
                        Text(
                            prayer.name,
                            style = if (isCurrent) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
                        )
                    },
                    supportingContent = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                prayerTime?.time?.format(getFormatter("HH:mm a")) ?: "00:00 am",
                                modifier = Modifier
                                    .placeholder(
                                        visible = prayerTime == null,
                                        highlight = PlaceholderHighlight.fade(),
                                        shape = RoundedCornerShape(100),
                                    ),
                            )

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

                Box(
                    modifier = Modifier.width(84.dp),
                    contentAlignment = Alignment.Center
                ) {
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
                                .size(64.dp)
                                .background(MaterialTheme.colorScheme.tertiaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(prayer.iconResId),
                                prayer.name,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
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
                                painterResource(prayer.iconResId),
                                prayer.name,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        expandedContent = {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
            ) {
                if (isCurrent) {
                    item {
                        ActionSectionChip(
                            "Snooze alert",
                            icon = IconSource(Icons.Outlined.Snooze),
                            modifier = Modifier.padding(
                                start = 16.dp,
                            )
                        )
                    }
                }
                if (isNotificationsAllowed) {
                    item {
                        ActionSectionChip(
                            "Manage notifications",
                            icon = IconSource(if (isSilent) Icons.Outlined.NotificationsOff else Icons.Outlined.Notifications),
                            modifier = Modifier.padding(
                                start = if (!isCurrent) 16.dp else 0.dp,
                                end = 16.dp
                            ),
                            onClick = {
                                navController.navigate("${Screen.Settings.Notifications.ManagePrayer.route}?prayer=${prayer.name}")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(8.dp))
            ListItemDivider()

            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (hasPassed && prayerTime?.prayer?.displayable == true && prayerRecordDetail?.prayed == false) {
                    val options = MissedReason.ALL.map { missedReason ->
                        Option(
                            title = missedReason?.name ?: "None",
                            icon = missedReason?.iconResId?.let { IconSource(it) },
                            selected = prayerRecordDetail.missedReason == missedReason,
                        )
                    }

                    SettingsListItem(
                        title = "Missed due to",
                        icon = IconSource(Icons.AutoMirrored.Outlined.CallMissedOutgoing),
                        extendedContent = {
                            OptionRow(options) { index, _ ->
                                onPrayerRecordDetailValueChange(null, null, MissedReason.ALL[index])
                            }
                        },
                        isLastItem = true
                    )
                }

                SettingsListItem(
                    icon = IconSource(Icons.AutoMirrored.Outlined.TextSnippet),
                    trailingContent = {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = prayerRecordDetail?.notes ?: "",
                            label = { Text("Notes") },
                            placeholder = { Text("Reason missed • Raka'ats prayed • Du'as read") },
                            onValueChange = {
                                onPrayerRecordDetailValueChange(
                                    null,
                                    it,
                                    null
                                ) // FIXME: Probably very inefficient!!! updating database on change
                            },
                            textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                            maxLines = 2,
                        )
                    },
                    isLastItem = true
                )
            }
        },
        expand = expand,
    )
}


@Preview(
    showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun Preview() {
    val navController = TestNavHostController(LocalContext.current)
//    navController.navigatorProvider.addNavigator(ComposeNavigator())

    Column(Modifier.padding(15.dp)) {
        PrayerCard(
            Prayer.FAJR,
            prayerTime = PrayerTime(
                Prayer.FAJR,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
            ),
            prayerRecordDetail = PrayerRecordDetail(Prayer.FAJR, false),
            remainingTimePercentage = 0.5f,
            isCurrent = true,
            isExpanded = true,
            hasPassed = true,
            expand = {},
            navController = navController
        )

        PrayerCard(
            Prayer.DHUHR,
            prayerTime = null,
//            prayerTime = PrayerTime(
//                Prayer.DHUHR,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(10)
//            ),
            prayerRecordDetail = PrayerRecordDetail(Prayer.DHUHR, false),
            isCurrent = true,
            isSilent = true,
            remainingTimePercentage = 0.5f,
            isExpanded = false,
            isAfterExpandedItem = true,
            expand = {},
            navController = navController
        )
        PrayerCard(
            Prayer.ASR,
            prayerTime = null,
            prayerRecordDetail = PrayerRecordDetail(Prayer.ASR, false),
            remainingTimePercentage = 0.5f,
            isExpanded = false,
            isLastItem = true,
            expand = {},
            navController = navController
        )
    }
}
