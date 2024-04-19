package com.example.domes.presentation.routes.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.composables.SettingsListItem

class UIConstants {
    companion object {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Settings",
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Prayers.route) }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },

        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
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
                        SettingsListItem(
                            "Location",
                            "Auto-update • set manually",
                            Icons.Outlined.LocationOn,
                            onClick = { navController.navigate(Screen.Location.route) },
                        )
                        SettingsListItem(
                            "Calculation",
                            "Method • corrections",
                            Icons.Outlined.Calculate,
                            onClick = { navController.navigate(Screen.Calculation.route) },
                        )
                        SettingsListItem(
                            "Notifications",
                            "Adhan • prayers",
                            Icons.Outlined.Notifications,
                            onClick = { navController.navigate(Screen.Notifications.route) },
                        )
                        SettingsListItem(
                            "Appearance",
                            "Theme • language • format",
                            Icons.Outlined.Palette,
                        )
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
                        SettingsListItem(
                            "About",
                            "App version • contact",
                            Icons.Outlined.Info,
                        )
                        SettingsListItem(
                            "Privacy",
                            "Policy • terms",
                            Icons.Outlined.Security,
                        )
                        SettingsListItem(
                            "Feedback",
                            "Report a bug • suggest a feature",
                            Icons.Outlined.QuestionAnswer,
                        )
                    }
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.Q)
//@Preview(
//    showBackground = true,
//    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
//)
//@Composable
//fun DomesPreview() {
//    DomesTheme {
//        SettingsScreen()
//    }
//}