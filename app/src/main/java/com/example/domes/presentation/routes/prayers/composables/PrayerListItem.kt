package com.example.domes.presentation.routes.prayers.composables

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.domes.domain.model.prayer.MissedReason
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerRecordDetail
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.presentation.common.composables.shape.Circle
import com.example.domes.presentation.common.composables.shape.Shape
import com.example.domes.presentation.common.utility.RoundedPolygonShape
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import java.time.LocalDateTime

// TODO: Rewrite to be more compact
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerListItem(
    prayerTime: PrayerTime,
    prayerRecordDetail: PrayerRecordDetail?,
    isExpanded: Boolean = false,
    isCurrent: Boolean = false,
    hasPassed: Boolean = false,
    isSilent: Boolean = false,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    remainingTimePercentage: Float,
    onExpand: (Boolean) -> Unit,
    onTick: (Boolean?, String?, MissedReason?) -> Unit = { prayed: Boolean?, notes: String?, missedReason: MissedReason? -> }
) {
    println("EXPANDED: ${prayerTime.prayer.name} - $isExpanded")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize()
            .background(
                if (isExpanded) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
//                MaterialTheme.shapes.large
            ),
        contentAlignment = Alignment.Center
    ) {
//        AnimatedVisibility(isExpanded.value, modifier = Modifier.matchParentSize()) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(8.dp)
//                    .background(
//                        MaterialTheme.colorScheme.surfaceVariant,
//                        MaterialTheme.shapes.large
//                    )
//            )
//        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                    .alpha(if (hasPassed && !isCurrent && !isExpanded) 0.5f else 1f)
                    .clickable {
                        onExpand(isExpanded)
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
                                prayerRecordDetail?.let { onTick(!it.prayed, null, null) }
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
            if (isExpanded) {
                Divider()
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (hasPassed && prayerRecordDetail?.prayed == false) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text("Missed due to")
//
//                            AssistChip(
//                                leadingIcon = { Icon(Icons.Outlined.Flight, "Travel") },
//                                label = { Text("Travel") },
//                                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, "Select") },
//                                onClick = { /*TODO*/ }
//                            )
//                        }

                        ExposedDropdownMenuBox(
                            modifier = Modifier.fillMaxWidth(),
                            expanded = true,
                            onExpandedChange = {}
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                value = prayerRecordDetail.missedReason?.name ?: "",
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = { Text("Missed due to") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = true) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = true,
                                onDismissRequest = { },
                            ) {
                                MissedReason.ALL.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it.name) },
                                        leadingIcon = {
                                            Icon(
                                                painterResource(it.iconResId),
                                                it.name
                                            )
                                        },
                                        onClick = { },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = "",
                        onValueChange = {},
                        leadingIcon = { Icon(Icons.AutoMirrored.Outlined.TextSnippet, null) },
                        label = { Text("Notes") },
                        placeholder = { Text("Reason missed • Raka'ats prayed • Du'as read") },
                        minLines = 2,
                    )

//                InputView(
//                    useCaseState = rememberUseCaseState(
//                        visible = true,
//                        embedded = true,
//                        onCloseRequest = {
//                            println("INPUT VIEW CLOSED")
//                        }
//                    ),
//                    selection = InputSelection(
//                        input = listOf(
//                            InputTextField(
//                                header = InputHeader(
//                                    title = "Notes",
//                                    icon = IconSource(Icons.AutoMirrored.Outlined.TextSnippet)
//                                ),
////                            columns = 3,
//                            maxLines = 5,
//                                type = InputTextFieldType.OUTLINED,
//                                validationListener = { value ->
//                                    ValidationResult.Valid
//                                }
//                            )
//                        ),
//                        withButtonView = false
//                    )
//                )
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun Preview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(
                    1.dp
                ), RoundedCornerShape(35.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        PrayerListItem(
            prayerTime = PrayerTime(
                Prayer.FAJR,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
            ),
            prayerRecordDetail = PrayerRecordDetail(Prayer.FAJR, false),
            remainingTimePercentage = 0.5f,
            onExpand = {}
        )
    }
}
