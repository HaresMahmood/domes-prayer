package com.example.domes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateBefore
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.NotificationsPaused
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import com.example.domes.ui.theme.DomesTheme
import com.example.domes.util.PolygonComposable
import com.example.domes.util.RoundedPolygonShape
import com.example.domes.util.ShapeParameters
import com.example.domes.util.scaled
import com.example.domes.util.toPath
import kotlinx.coroutines.launch
import kotlin.math.min

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
    isPassed: Boolean = false,
    isActive: Boolean = false,
) {
    ListItem(
        modifier = Modifier
            .padding(start = 16.dp, end = if (isActive) 0.dp else 16.dp), // TODO: A bit weird
        leadingContent = {
            IconButton(onClick) {
                Icon(Icons.Outlined.RadioButtonUnchecked, "Done")
            }
        },
        headlineContent = {
            // use the isActive property to conditionally apply the bold font
            Text(prayerName, style = if (isActive) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge)
        },
        supportingContent = {
            Text(prayerTime) // , style = MaterialTheme.typography.labelSmall
        },
        trailingContent = {
            // use the isActive property to conditionally apply the clip shape or the circle shape
            if (isActive) {
                // use the clip parameter to clip the icon by the polygon shape
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
                // use the Box function to draw the circle shape
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    when (icon) {
                        is ImageVector -> Icon(icon, prayerName)
                        is Painter -> Icon(icon, prayerName)
                        else -> throw IllegalArgumentException("Invalid icon type")
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val sheetPeekHeight = 100.dp;

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetDragHandle = { null },
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetPeekHeight),
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
                                color = MaterialTheme.colorScheme.tertiary, // if (entry.date == LocalDate.now()) MaterialTheme.colorScheme.primary else 
                            )
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "17",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
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
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Row(
                                modifier = Modifier
                                    .padding(end = 15.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Asr",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "2:30 left",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        },
                        actions = {
                            // TODO: Use something other than chip?
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
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent, // MaterialTheme.colorScheme.surfaceContainer,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { },
                        icon = { Icons.Filled.Explore },
                        text = { Text(text = "Find Qibla") }
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Column {
                        LinearProgressIndicator(
                            progress = { 0.65f },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
//                                .padding(start = 16.dp, end = 16.dp, top = 5.dp),
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
                                Column {
                                    PrayerTimeListItem(
                                        prayerName = "Fajr",
                                        icon = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24),
                                        prayerTime = "5:30 AM",
                                        isPassed = true,
                                        onClick = { /*TODO*/ }
                                    )
                                    PrayerTimeListItem(
                                        prayerName = "Sunrise",
                                        icon = Icons.Outlined.WbSunny,
                                        prayerTime = "12:30 PM",
                                        isPassed = true,
                                        onClick = { /*TODO*/ }
                                    )
                                    PrayerTimeListItem(
                                        prayerName = "Dhuhr",
                                        icon = Icons.Outlined.WbSunny,
                                        prayerTime = "12:30 PM",
                                        isPassed = true,
                                        onClick = { /*TODO*/ }
                                    )
                                    PrayerTimeListItem(
                                        prayerName = "Asr",
                                        icon = Icons.Outlined.WbSunny,
                                        prayerTime = "4:30 PM",
                                        isActive = true,
                                        onClick = { /*TODO*/ }
                                    )
                                    /*
                                    OutlinedCard(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.secondary,
                                        )
                                    ) {
                                        ListItem(
                                            //                            modifier = Modifier
                                            //                                .border(
                                            //                                    width = 2.dp,
                                            //                                    color = MaterialTheme.colorScheme.secondary,
                                            //                                    shape = RoundedCornerShape(10.dp)
                                            //                                )
                                            //                                .clip(RoundedCornerShape(10.dp)),
                                            leadingContent = {
                                                Icon(
                                                    Icons.Outlined.WbSunny,
                                                    contentDescription = "Location"
                                                )
                                            },
                                            headlineContent = {
                                                Text(
                                                    text = "Asr",
                                                    fontWeight = FontWeight.Medium
                                                )
                                            },
                                            //                        supportingContent = { Text(text = "3:30 PM · Silent") },
                                            trailingContent = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                                ) {
                                                    Text(
                                                        text = "4:30 PM",
                                                        style = MaterialTheme.typography.labelSmall,
                                                    )
                                                    IconButton(onClick = { /*TODO*/ }) {
                                                        Icon(
                                                            Icons.Outlined.RadioButtonUnchecked,
                                                            contentDescription = "Done"
                                                        )
                                                    }
                                                }
                                            },
                                            colors = ListItemDefaults.colors(
                                                containerColor = Color.Transparent,
                                            )
                                        )

                                        Row() {
                                            Button(onClick = { /*TODO*/ }) {
                                                Icon(
                                                    Icons.Outlined.NotificationsPaused,
                                                    contentDescription = "Snooze alert"
                                                )
                                                Text(text = "Snooze alert")
                                            }
                                        }
                                    }
                                    */
                                    PrayerTimeListItem(
                                        prayerName = "Maghrib",
                                        icon = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24),
                                        prayerTime = "6:30 PM",
                                        onClick = { /*TODO*/ }
                                    )
                                    PrayerTimeListItem(
                                        prayerName = "Isha",
                                        icon = Icons.Outlined.NightsStay,
                                        prayerTime = "8:30 PM",
                                        onClick = { /*TODO*/ }
                                    )
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