package com.example.domes.presentation.settings.routes.calculation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.common.composables.LayeredTopAppBar
import com.example.domes.ui.theme.DomesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class UIConstants {
    companion object {

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CalculationScreen(
//    navController: NavController,
//    viewModel: CalculationViewModel = hiltViewModel()
) {
//    val state = viewModel.state.value

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LayeredTopAppBar(
                "Calculation",
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
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(
                            topStartPercent = 12,
                            topEndPercent = 45,
                            bottomStartPercent = 45,
                            bottomEndPercent = 45
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        ),
                    ) {
                        Icon(Icons.Outlined.PinDrop, "Current location")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("State")
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
                        // TODO: Create unified ListItem composable
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 5.dp,
                                    end = 5.dp,
                                )
                                .clickable {

                                },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Calculate,
                                    "Calculation method",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Calculation method") },
                            supportingContent = {
                                Text(
                                    "Muslim World League",
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            },
                        )
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 5.dp,
                                    end = 5.dp,
                                    bottom = 8.dp,
                                ),
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.AccountBalance,
                                    "Juristic",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            headlineContent = { Text("Juristic") },
                            supportingContent = {
                                Text(
                                    "Hanafi",
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun CalculationMethodDialog(

) {
    
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        CalculationScreen()
    }
}