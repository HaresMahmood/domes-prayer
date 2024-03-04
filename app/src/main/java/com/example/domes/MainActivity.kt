package com.example.domes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domes.ui.theme.DomesTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 128.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    // Make height the height of content
                    .height(intrinsicSize = IntrinsicSize.Max),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column() {
                        Text(text = "August 2024")
                        Text(text = "Muharram 28 1446")
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
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "2h 30 mins left",
                                    style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        actions = {
                            // TODO: Use something other than chip?
                            AssistChip(
                                onClick = { /*TODO*/ },
                                label = { Text(text = "Glasgow, UK" ) },
                                leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = "Location") },
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
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                        ),
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            ListItem(
                                modifier = Modifier.alpha(0.5f),
                                leadingContent = { Icon(painter = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24), contentDescription = "Location") },
                                headlineContent = { Text(text = "Fajr") },
//                        supportingContent = { Text(text = "5:30 AM") },
                                trailingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "5:30 AM",
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(Icons.Outlined.CheckCircle, contentDescription = "Done")
                                        }
                                    }
                                }
                            )
                            ListItem(
                                modifier = Modifier.alpha(0.5f),
                                leadingContent = { Icon(Icons.Outlined.WbTwilight, contentDescription = "Location") },
                                headlineContent = { Text(text = "Sunrise") },
//                        supportingContent = { Text(text = "5:30 AM") },
                                trailingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "7:30 PM",
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                        IconButton(onClick = { /*TODO*/ }, enabled = false) {
                                            Icon(Icons.Outlined.RadioButtonChecked, contentDescription = "None")
                                        }
                                    }
                                }
                            )
                            ListItem(
                                leadingContent = { Icon(Icons.Outlined.WbSunny, contentDescription = "Location") },
                                headlineContent = { Text(text = "Dhuhr") },
//                        supportingContent = { Text(text = "5:30 AM") },
                                trailingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "12:30 PM",
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
                                        }
                                    }
                                }
                            )
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
                                    leadingContent = { Icon(Icons.Outlined.WbSunny, contentDescription = "Location") },
                                    headlineContent = { Text(text = "Asr", fontWeight = FontWeight.Medium) },
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
                                                Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
                                            }
                                        }
                                    },
                                    colors = ListItemDefaults.colors(
                                        containerColor = Color.Transparent,
                                    )
                                )

                                Row() {
                                    Button(onClick = { /*TODO*/ }) {
                                        Icon(Icons.Outlined.NotificationsPaused, contentDescription = "Snooze alert")
                                        Text(text = "Snooze alert")
                                    }
                                }
                            }
                            ListItem(
                                leadingContent = { Icon(painter = painterResource(R.drawable.routine_fill0_wght400_grad0_opsz24), contentDescription = "Location") },
                                headlineContent = { Text(text = "Maghrib") },
//                        supportingContent = { Text(text = "5:30 AM") },
                                trailingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "6:30 PM",
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
                                        }
                                    }
                                }
                            )
                            ListItem(
                                leadingContent = { Icon(Icons.Outlined.NightsStay, contentDescription = "Location") },
                                headlineContent = { Text(text = "Isha") },
//                        supportingContent = { Text(text = "5:30 AM") },
                                trailingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "8:30 PM",
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
                                        }
                                    }
                                }
                            )
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