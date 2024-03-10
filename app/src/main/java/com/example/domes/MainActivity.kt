package com.example.domes

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.domes.data.PrayerTime
import com.example.domes.ui.theme.DomesTheme
import com.example.domes.util.RoundedPolygonShape
import com.example.domes.util.ShapeParameters
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
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
            isPassed = true,
            onClick = { /*TODO*/ }
        ),
        PrayerTime(
            prayerName = "Maghrib",
            icon = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24),
            prayerTime = "6:30 PM",
            isActive = true,
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
        sheetContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
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
                                Icons.Outlined.NavigateBefore,
                                contentDescription = "Previous month"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Outlined.NavigateNext,
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
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp)
                                    .align(Alignment.BottomStart),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom,
                            ) {
                                Box(
                                    Modifier
                                        .height(66.dp)
                                ) {
                                    Box(
                                        Modifier
                                            //                                            .height(64.0.dp)
                                            .offset(8.dp, 8.dp)
                                            .alpha(0.5f),
                                        //                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "مغرب",
                                            style = MaterialTheme.typography.displayMedium,
                                            color = MaterialTheme.colorScheme.outlineVariant,
                                            fontFamily = FontFamily(Font(R.font.alexandira_arabic_variable)),
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }

                                    Text(
                                        text = "Maghrib",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = FontFamily(Font(R.font.alexandira_arabic_variable)),
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                            }
                        },
                        actions = {
                            Row(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                            ) {
                                AssistChip(
                                    onClick = { /*TODO*/ },
                                    label = { Text(text = "Glasgow") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.LocationOn,
                                            "Location"
                                        )
                                    },
                                    border = AssistChipDefaults.assistChipBorder(
                                        Color.Transparent
                                    ),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    ),
                                )

//                                IconButton(
//                                    modifier = Modifier
//                                        .padding(start = 12.dp),
//                                    onClick = { /*TODO*/ }
//                                ) {
//                                    Icon(Icons.Outlined.MoreVert, "Options")
//                                }
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                        )
                    ) {
                        Icon(Icons.Filled.Explore, "Find Qibla")
                    }
                },
            ) { innerPadding ->
                var sliderPosition by remember { mutableFloatStateOf(25f) }

                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
//                        Slider(
//                            value = sliderPosition,
//                            onValueChange = { sliderPosition = it },
//                            colors = SliderDefaults.colors(
//                                thumbColor = MaterialTheme.colorScheme.secondary,
//                                activeTrackColor = MaterialTheme.colorScheme.secondary,
//                                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
//                            ),
//                            steps = 3,
//                            valueRange = 0f..50f
//                        )
                        /*
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(175.dp)
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

                            Box(
                                modifier = Modifier
                                    .clip(clip)
                                    .size(150.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(MaterialTheme.colorScheme.surfaceTint),
                            )
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
                                    border = AssistChipDefaults.assistChipBorder(
                                        Color.Transparent
                                    ),
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
                                    border = AssistChipDefaults.assistChipBorder(
                                        Color.Transparent
                                    ),
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
                                    border = AssistChipDefaults.assistChipBorder(
                                        Color.Transparent
                                    ),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        trailingIconContentColor = MaterialTheme.colorScheme.tertiary,
                                    ),
                                )
                            }
                        }
                        */
                        
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
//                            item {
//                                Text(
//                                    text = "Schedule",
//                                    style = MaterialTheme.typography.titleLarge,
////                                    fontWeight = FontWeight.Bold
//                                )
//                            }

                            item {
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

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                1.dp
                                            ), RoundedCornerShape(35.dp)
                                        ),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
//                                    IconToggleButton(
//                                        onCheckedChange = { /* TODO */ },
//                                        checked = true,
//                                        colors = IconButtonDefaults.iconToggleButtonColors(
//                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                                        ),
//                                        modifier = Modifier
//                                            .align(Alignment.End)
//                                            .padding(end = 8.dp, top = 8.dp),
//                                    ) {
//                                        Icon(
//                                            Icons.Outlined.ZoomIn,
//                                            "Location"
//                                        )
//                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 24.dp,
                                                end = 24.dp,
                                                top = 16.dp,
                                                bottom = 16.dp
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
//                                        PercentageWidthBox(
////                                            modifier = Modifier
////                                                .fillMaxWidth()
////                                                .height(20.dp),
//                                            children = listOf(
//                                                PercentageWidthBoxChild(
//                                                    composable = {
//                                                        Box(
//                                                            modifier = Modifier
//                                                                .clip(clip)
//                                                                .size(20.dp)
//                                                                .alpha(0.5f)
//                                                                .background(MaterialTheme.colorScheme.onSecondaryContainer),
//                                                        )
//                                                                 },
//                                                    parentContainerHeight = 50.dp,
//                                                    widthFraction = 0.23f
//                                                ),
//                                                PercentageWidthBoxChild(
//                                                    composable = {
//                                                        Box(
//                                                            modifier = Modifier
//                                                                .clip(clip)
//                                                                .size(20.dp)
//                                                                .alpha(0.5f)
//                                                                .background(MaterialTheme.colorScheme.onSecondaryContainer),
//                                                        )
//                                                    },
//                                                    parentContainerHeight = 50.dp,
//                                                    widthFraction = 0.55f
//                                                ),
//                                                // Add more children as needed
//                                            )
//                                        )

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(0.77f)
                                                    .height(20.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.secondaryContainer,
                                                        RoundedCornerShape(35.dp)
                                                    ),
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .width(4.dp)
                                                    .height(32.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.primary,
                                                        RoundedCornerShape(35.dp)
                                                    ),
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(20.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.primary,
                                                        RoundedCornerShape(35.dp)
                                                    ),
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(20.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.primary,
                                                        RoundedCornerShape(35.dp)
                                                    ),
                                            )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth(0.23f)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(clip)
                                                        .size(15.dp)
                                                        .align(Alignment.End)
                                                        .alpha(0.5f)
                                                        .background(MaterialTheme.colorScheme.onSecondaryContainer),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            item {
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

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        MainScreen()
    }
}