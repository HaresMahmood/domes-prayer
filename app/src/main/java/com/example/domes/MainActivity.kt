package com.example.domes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domes.ui.theme.DomesTheme

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
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Row(
                        modifier = Modifier
//                            .padding()
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
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
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
        bottomBar = {
//            NavigationBar {
//                NavigationBarItem(
//                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
//                    label = { Text(text = "Home") },
//                    selected = true,
//                    onClick = { /*TODO*/ }
//                )
//                NavigationBarItem(
//                    icon = { Icon(Icons.Outlined.Explore, contentDescription = "Qibla") },
//                    label = { Text(text = "Qibla") },
//                    selected = false,
//                    onClick = { /*TODO*/ }
//                )
//                NavigationBarItem(
//                    icon = { Icon(Icons.Outlined.EventAvailable, contentDescription = "Track") },
//                    label = { Text(text = "Track") },
//                    selected = false,
//                    onClick = { /*TODO*/ }
//                )
//            }
        }
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
                    ListItem(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp)),
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
                            headlineColor = MaterialTheme.colorScheme.secondary,
                            supportingColor = MaterialTheme.colorScheme.secondary,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DomesTheme {
        MainScreen()
    }
}