package com.example.domes.presentation.routes.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.example.domes.ui.theme.DomesTheme
import com.maxkeppeker.sheets.core.models.base.IconSource

class UIConstants {
    companion object
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    FixedCard {
                        SettingsListItem(
                            "Location",
                            IconSource(Icons.Outlined.LocationOn),
                            "Auto-update • set manually",
                            onClick = { navController.navigate(Screen.Location.route) },
                        )
                        SettingsListItem(
                            "Calculation",
                            IconSource(Icons.Outlined.Calculate),
                            "Method • juristic",
                            onClick = { navController.navigate(Screen.Calculation.route) },
                        )
                        SettingsListItem(
                            "Notifications",
                            IconSource(Icons.Outlined.Notifications),
                            "Adhan • prayers • corrections",
                            onClick = { navController.navigate(Screen.Notifications.route) },
                        )
                        SettingsListItem(
                            "Integrations",
                            IconSource(Icons.Outlined.AccountTree),
                            "Calendar • Google Nest",
                            onClick = { navController.navigate(Screen.Integrations.route) },
                        )
                        SettingsListItem(
                            "Widgets",
                            IconSource(Icons.Outlined.Widgets),
                            "Home screen widgets",
                            isEnabled = false,
                        )
                        SettingsListItem(
                            "Appearance",
                            IconSource(Icons.Outlined.Palette),
                            "Theme • language • format",
                            isLastItem = true,
                            onClick = { navController.navigate(Screen.Appearance.route) },
                        )
                    }
                }
                item {
                    FixedCard {
                        SettingsListItem(
                            "About",
                            IconSource(Icons.Outlined.Info),
                            "App version • contact",
                        )
                        SettingsListItem(
                            "Privacy",
                            IconSource(Icons.Outlined.Security),
                            "Policy • terms",
                        )
                        SettingsListItem(
                            "Feedback",
                            IconSource(Icons.Outlined.QuestionAnswer),
                            "Report a bug • suggest a feature",
                            isLastItem = true
                        )
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
fun SettingsPreview() {
    val navController = rememberNavController()

    DomesTheme {
        SettingsScreen(navController)
    }
}