package com.example.domes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TimeToLeave
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
                    Text(
                        text = "Asr",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text(text = "Home") },
                    selected = true,
                    onClick = { /*TODO*/ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Explore, contentDescription = "Qibla") },
                    label = { Text(text = "Qibla") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.EventAvailable, contentDescription = "Track") },
                    label = { Text(text = "Track") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
            }
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    AssistChip(
                        leadingIcon = { Icon(Icons.Outlined.Timer, contentDescription = "Time") },
                        label = { Text(text = "2 hrs 30 mins until Maghrib") },
                        onClick = { /*TODO*/ }
                    )
                    AssistChip(
                        leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = "Location") },
                        label = { Text(text = "Glasgow, UK") },
                        onClick = { /*TODO*/ }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    ListItem(
                        modifier = Modifier.alpha(0.5f),
                        headlineText = { Text(text = "Fajr") },
                        supportingText = { Text(text = "5:30 AM") },
                        trailingContent = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Outlined.CheckCircle, contentDescription = "Done")
                            }
                        }
                    )
                    ListItem(
                        modifier = Modifier.alpha(0.5f),
                        headlineText = { Text(text = "Dhuhr") },
                        supportingText = { Text(text = "12:30 PM") },
                        trailingContent = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
                            }
                        }
                    )
                    ListItem(
                        modifier = Modifier.border(width = 2.dp, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp)),
                        headlineText = { Text(text = "Asr", fontWeight = FontWeight.Medium) },
                        supportingText = { Text(text = "3:30 PM · Silent") },
                        trailingContent = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
                            }
                        },
                        colors = ListItemDefaults.colors(
                            headlineColor = MaterialTheme.colorScheme.secondary,
                            supportingColor = MaterialTheme.colorScheme.secondary,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                    ListItem(
                        headlineText = { Text(text = "Maghrib") },
                        supportingText = { Text(text = "6:00 PM") },
                        trailingContent = {
                            IconButton(onClick = { /*TODO*/ }, enabled = false) {
                                Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
                            }
                        }
                    )
                    ListItem(
                        headlineText = { Text(text = "Isha") },
                        supportingText = { Text(text = "7:30 PM") },
                        trailingContent = {
                            IconButton(onClick = { /*TODO*/ }, enabled = false) {
                                Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Done")
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