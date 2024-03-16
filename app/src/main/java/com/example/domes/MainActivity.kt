package com.example.domes

import android.os.Build
import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.domes.data.PrayerTime
import com.example.domes.ui.composables.DisplaySlider
import com.example.domes.ui.composables.PercentageWidthBox
import com.example.domes.ui.composables.PercentageWidthBoxChild
import com.example.domes.ui.composables.Polygon
import com.example.domes.ui.composables.PrayerTopAppBar
import com.example.domes.ui.theme.DomesTheme
import com.example.domes.util.RoundedPolygonShape
import com.example.domes.util.ShapeParameters
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarView
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.time.LocalDate

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
            Text(
                prayerName,
                style = if (isActive) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            Row(
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val sheetPeekHeight = 100.dp

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val prayerTimes = listOf(
        PrayerTime(
            prayerName = "Fajr",
            icon = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24),
            prayerTime = "5:30 AM",
            dayFraction = 0.23f, // 5.5f / 24,
            isPassed = true,
        ),
        PrayerTime(
            prayerName = "Sunrise",
            icon = Icons.Outlined.WbSunny,
            prayerTime = "7:30 PM",
            isSilent = true,
            dayFraction = 0.31f, // 7.5f / 24,
            isPassed = true,
        ),
        PrayerTime(
            prayerName = "Dhuhr",
            icon = Icons.Outlined.WbSunny,
            prayerTime = "12:30 PM",
            dayFraction = 0.52f, // 12.5f / 24,
            isPassed = true,
        ),
        PrayerTime(
            prayerName = "Asr",
            icon = Icons.Outlined.WbSunny,
            prayerTime = "4:30 PM",
            dayFraction = 0.68f, // 16.5f / 24,
            isPassed = true,
        ),
        PrayerTime(
            prayerName = "Maghrib",
            icon = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24),
            prayerTime = "6:30 PM",
            dayFraction = 0.78f, // 18.5f / 24,
            isActive = true,
        ),
        PrayerTime(
            prayerName = "Isha",
            icon = Icons.Outlined.NightsStay,
            dayFraction = 0.85f, // 20.5f / 24,
            prayerTime = "8:30 PM",
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
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

                        Column {
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

                    Row {
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
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

                CalendarView(
                    useCaseState = rememberUseCaseState(visible = true),
                    selection = CalendarSelection.Date(
                        selectedDate = selectedDate.value
                    ) { newDate ->
                        selectedDate.value = newDate
                    },
                )
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
                topBar = { PrayerTopAppBar("Maghrib", "مغرب", scrollBehavior) },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { },
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        stickyHeader {
                            LazyRow(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val buttonCornerShapes = RoundedCornerShape(15)
                                val buttonPaddingValues = PaddingValues(horizontal = 14.dp)

                                item {
                                    Button(
                                        onClick = { /*TODO*/ },
                                        shape = RoundedCornerShape(topStartPercent = 12, topEndPercent = 45, bottomStartPercent = 45, bottomEndPercent = 45),
                                        contentPadding = buttonPaddingValues,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        ),
                                    ) {
                                        Icon(Icons.Outlined.HourglassTop,  "Time left")
                                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                        Text("2h 30 min left")
                                    }
                                }
                                item {
                                    Button(
                                        onClick = { /*TODO*/ },
                                        shape = buttonCornerShapes,
                                        contentPadding = buttonPaddingValues,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        ),
                                    ) {
                                        Icon(Icons.Outlined.LocationOn, "Location")
                                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                        Text(text = "Glasgow, UK")
                                    }
                                }
                                item {
                                    Button(
                                        onClick = { /*TODO*/ },
                                        shape = buttonCornerShapes,
                                        contentPadding = buttonPaddingValues,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        ),
                                    ) {
                                        Icon(Icons.Outlined.Snooze, "Snooze alert")
                                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                        Text(text = "Snooze alert")
                                    }
                                }
                                item {
                                    Button(
                                        onClick = { /*TODO*/ },
                                        shape = RoundedCornerShape(100),
                                        contentPadding = buttonPaddingValues,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        ),
                                    ) {
                                        Icon(painterResource(R.drawable.menstrual_health_fill0_wght400_grad0_opsz24), "Period")
                                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                        Text(text = "Period")
                                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                        Icon(Icons.Outlined.Check, "Selected")
                                    }
                                }
                                item {
                                    FilledTonalIconButton(
                                        onClick = { /*TODO*/ }
                                    ) {
                                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                                    }
                                }
                            }
                        }
                        item {
                            val shapeParam = ShapeParameters(
                                roundness = .4f,
                                sides = 4,
                                rotation = 90f,
                                shapeId = ShapeParameters.ShapeId.Polygon
                            )
                            val shape = shapeParam.genShape().normalized()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                                            1.dp
                                        ), RoundedCornerShape(35.dp)
                                    ),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
//
//                                        IconToggleButton(
//                                            onCheckedChange = { /* TODO */ },
//                                            checked = true,
//                                            colors = IconButtonDefaults.iconToggleButtonColors(
//                                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                                            ),
//                                            modifier = Modifier
////                                                .padding(end = 8.dp, top = 8.dp),
//                                        ) {
//                                            Icon(
//                                                Icons.Outlined.ViewDay,
//                                                "Location"
//                                            )
//                                        }
//                                    }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 16.dp,
                                            start = 24.dp,
                                            end = 24.dp,
                                            bottom = 16.dp
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DisplaySlider(
                                        sliderValue = 0.8f,
                                        text = "2h 30 min left",
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        onValueChange = { }
                                    )

                                    PercentageWidthBox(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(20.dp),
                                        children = prayerTimes.map { prayerTime ->
                                            PercentageWidthBoxChild(
                                                composable = {
                                                    // Your composable content here, e.g., an icon with a label
                                                    Box(
                                                        modifier = Modifier
                                                            .size(10.dp)
                                                    ) {
                                                        Polygon(
                                                            shape,
                                                            color =
                                                            if (prayerTime.isActive) MaterialTheme.colorScheme.onSecondaryContainer
                                                            else if (prayerTime.isPassed) MaterialTheme.colorScheme.onSecondaryContainer
                                                            else MaterialTheme.colorScheme.secondaryContainer,
                                                            alpha = if (prayerTime.isActive) 1f else 0.35f
                                                        )
                                                    }
                                                },
                                                widthFraction = prayerTime.dayFraction
                                            )
                                        }
                                    )
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
                                        onClick = { },
                                        isSilent = prayerTimeItem.isSilent,
                                        isPassed = prayerTimeItem.isPassed,
                                        isActive = prayerTimeItem.isActive,
                                        isFirstItem = index == 0,
                                        isLastItem = index == prayerTimes.lastIndex
                                    )
                                }
                            }
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
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        MainScreen()
    }
}