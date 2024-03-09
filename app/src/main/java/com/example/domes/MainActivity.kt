package com.example.domes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateBefore
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domes.data.PrayerTime
import com.example.domes.ui.theme.DomesTheme
import com.example.domes.util.RoundedPolygonShape
import com.example.domes.util.ShapeParameters
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DomesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun PrayerTimeListItem(
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
                start = if (isActive) 0.dp else 12.dp,
                end = 0.dp,
                top = if (isFirstItem) 16.dp else 0.dp,
                bottom = if (isLastItem) 16.dp else 0.dp,
            )
            .alpha(if (isPassed) 0.5f else 1f),
        leadingContent = {
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
//                Box(
//                    modifier = Modifier
//                        .size(38.dp)
//                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    when (icon) {
//                        is ImageVector -> Icon(icon, prayerName)
//                        is Painter -> Icon(icon, prayerName)
//                        else -> throw IllegalArgumentException("Invalid icon type")
//                    }
//                }

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
        headlineContent = {
            Text(prayerName, style = if (isActive) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge)
        },
        trailingContent = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                if (isSilent) {
                    Icon(
                        Icons.Outlined.NotificationsOff,
                        modifier = Modifier.width(18.dp),
                        contentDescription = "Silent"
                    )
                }

                Box(contentAlignment = Alignment.Center) {
                    if (isActive) {
                        CircularProgressIndicator(
                            progress = { 0.75f },
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
        supportingContent = {
            Text(prayerTime)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val sheetPeekHeight = 100.dp

    val prayerTimes = listOf(
        PrayerTime(
            prayerName = "Fajr",
            icon = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24),
            prayerTime = "5:30 AM",
            isPassed = true,
            onClick = { /*TODO*/ }
        ),
        PrayerTime(
            prayerName = "Sunrise",
            icon = Icons.Outlined.WbSunny,
            prayerTime = "12:30 PM",
            isSilent = true,
            isPassed = true,
            onClick = { /*TODO*/ }
        ),
        PrayerTime(
            prayerName = "Dhuhr",
            icon = Icons.Outlined.WbSunny,
            prayerTime = "12:30 PM",
            isPassed = true,
            onClick = { /*TODO*/ }
        ),
        PrayerTime(
            prayerName = "Asr",
            icon = Icons.Outlined.WbSunny,
            prayerTime = "4:30 PM",
            isActive = true,
            onClick = { /*TODO*/ }
        ),
        PrayerTime(
            prayerName = "Maghrib",
            icon = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24),
            prayerTime = "6:30 PM",
            onClick = { /*TODO*/ }
        ),
        PrayerTime(
            prayerName = "Isha",
            icon = Icons.Outlined.NightsStay,
            prayerTime = "8:30 PM",
            onClick = { /*TODO*/ }
        )
    )


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = RoundedCornerShape(35.dp),
        sheetDragHandle = { null },
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetPeekHeight)
                    .clickable {
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 26.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // A circle with the day of the month, and the corresponding weekday name above it
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "MON",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary, // if (entry.date == LocalDate.now()) MaterialTheme.colorScheme.primary else
                            )
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "17",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                )
                            }
                        }

                        Column() {
                            Text(
                                text = "August 2024",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "Muharram 28 1446",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }

                    Row() {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.NavigateBefore,
                                contentDescription = "Previous month"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.NavigateNext,
                                contentDescription = "Next month"
                            )
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//                topBar = {
//                    TopAppBar(
//                        title = {},
////                        actions = {
//////                            Icon(Icons.Outlined.Search, "Search")
////                                  Text(
////                                      "عصر",
////                                      style = MaterialTheme.typography.headlineLarge,
////                                  )
////                        },
//                    )
//                },
                floatingActionButton = {
                    SmallFloatingActionButton(
                        onClick = { },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                        )
                    ) {
                        Icon(Icons.Filled.Explore, "Find Qibla")
                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(175.dp)
                                .padding(horizontal = 26.dp),
                        ) {
                            val shapeParam = ShapeParameters(
                                sides = 15,
                                innerRadius = .892f,
                                roundness = 1f,
                                shapeId = ShapeParameters.ShapeId.Star
                            )
                            val shape = remember {
                                shapeParam.genShape()
                            }
                            val clip = remember(shape) {
                                RoundedPolygonShape(polygon = shape)
                            }

//                            Box(
//                                modifier = Modifier
//                                    .clip(clip)
//                                    .size(150.dp)
//                                    .align(Alignment.BottomEnd)
//                                    .background(MaterialTheme.colorScheme.surfaceDim),
//                            )

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                            ) {
                                /*
                                Box(
                                    Modifier.height(40.dp)
                                ) {
                                    Text(
                                        text = "عصر",
                                        style = MaterialTheme.typography.displayLarge,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
                                */

                                Box() {
                                    Text(
                                        text = "Asr",
                                        style = MaterialTheme.typography.displayLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = "2 hours 30 mins left",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            item {
                                FilledTonalIconButton(
                                    modifier = Modifier
                                        .padding(start = 12.dp),
                                    onClick = { /*TODO*/ }
                                ) {
                                    Icon(Icons.Outlined.MoreVert, contentDescription = "Settings")
                                }
                            }
                            item {
                                AssistChip(
                                    onClick = { /*TODO*/ },
                                    label = { Text(text = "Glasgow, UK") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.LocationOn,
                                            contentDescription = "Location"
                                        )
                                    },
                                    border = BorderStroke(0.dp, Color.Transparent),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    ),
                                )
                            }
                            item {
                                AssistChip(
                                    onClick = { /*TODO*/ },
                                    label = { Text(text = "Snooze alert") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Snooze,
                                            contentDescription = "Snooze"
                                        )
                                    },
                                    border = BorderStroke(0.dp, Color.Transparent),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    ),
                                )
                            }
                            item {
                                AssistChip(
                                    modifier = Modifier
                                        .padding(end = 12.dp),
                                    onClick = { /*TODO*/ },
                                    label = { Text(text = "Period") },
                                    leadingIcon = {
                                        Icon(
                                            painterResource(R.drawable.menstrual_health_fill0_wght400_grad0_opsz24),
                                            "Period",
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Outlined.Check,
                                            "Selected",
                                        )
                                    },
                                    border = BorderStroke(0.dp, Color.Transparent),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        trailingIconContentColor = MaterialTheme.colorScheme.tertiary,
                                    ),
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
//                            item {
//                                Text(
//                                    text = "Schedule",
//                                    style = MaterialTheme.typography.titleLarge,
////                                    fontWeight = FontWeight.Bold
//                                )
//                            }
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(35.dp)),
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    prayerTimes.forEachIndexed { index, prayerTimeItem ->
                                        PrayerTimeListItem(
                                            prayerName = prayerTimeItem.prayerName,
                                            icon = prayerTimeItem.icon,
                                            prayerTime = prayerTimeItem.prayerTime,
                                            onClick = prayerTimeItem.onClick,
                                            isSilent = prayerTimeItem.isSilent,
                                            isPassed = prayerTimeItem.isPassed,
                                            isActive = prayerTimeItem.isActive,
                                            isFirstItem = index == 0,
                                            isLastItem = index == prayerTimes.lastIndex
                                        )
                                    }
                                }
                            }
//                            item {
//                                HorizontalDivider()
//                            }
//                            item {
//                                Text(
//                                    text = "Trends",
//                                    style = MaterialTheme.typography.titleLarge,
//                                )
//                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DomesPreview() {
    DomesTheme {
        MainScreen()
    }
}