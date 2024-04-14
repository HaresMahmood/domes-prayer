package com.example.domes.presentation.routes.settings.routes.notifications

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.LayeredTopAppBar
import com.example.domes.presentation.common.utility.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Notifications",
                navigationIcon = Icons.AutoMirrored.Outlined.ArrowBack,
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
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(35.dp)
                            ),
                    ) {
                        // TODO: Add confirmation dialog
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 5.dp,
                                    end = 5.dp,
                                    bottom = 8.dp
                                )
                                .clickable {
                                    viewModel.onEvent(CalculationEvent.ResetSettings())
                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                                headlineColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Notifications,
                                    "Notifications",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Allow notifications") },
                            trailingContent = {
                                Switch(
                                    checked = true,
                                    onCheckedChange = { }
                                )
                            }
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
                    ) {
                        Prayer.ALL.forEach { prayer ->
                            ListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 5.dp,
                                        end = 5.dp,
                                    )
                                    .clickable {
                                        navController.navigate(Screen.ManagePrayer.route + "?prayer=${prayer.name}")
                                    },
                                colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent,
                                ),
                                leadingContent = {
                                    Icon(
                                        painterResource(prayer.iconResId),
                                        "Prayer time",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                },
                                headlineContent = { Text(prayer.name) },
                                supportingContent = {
                                    Text(
                                        "3 notifications",
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                },
                                trailingContent = {
                                    Row(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        Divider(
                                            color = MaterialTheme.colorScheme.outlineVariant,
                                            modifier = Modifier
                                                .height(SwitchDefaults.IconSize * 2) // TODO: This is kinda dumb
                                                .width(1.dp)
                                        )
                                        Switch(
                                            checked = true,
                                            onCheckedChange = { }
                                        )
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

//@RequiresApi(Build.VERSION_CODES.Q)
//@Preview(
//    showBackground = true,
//    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
//)
//@Composable
//fun DomesPreview() {
//    DomesTheme {
//        NotificationsScreen()
//    }
//}