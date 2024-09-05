package com.example.domes.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.FilePresent
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.domes.data.worker.PrayerTimesUpdateWorker
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.PrayersScreen
import com.example.domes.presentation.routes.prayers.PrayersViewModel
import com.example.domes.presentation.routes.qibla.QiblaScreen
import com.example.domes.presentation.routes.settings.Category
import com.example.domes.presentation.routes.settings.Setting
import com.example.domes.presentation.routes.settings.SettingsScreen
import com.example.domes.presentation.routes.settings.routes.appearance.AppearanceScreen
import com.example.domes.presentation.routes.settings.routes.calculation.CalculationScreen
import com.example.domes.presentation.routes.settings.routes.integrations.IntegrationsScreen
import com.example.domes.presentation.routes.settings.routes.location.LocationScreen
import com.example.domes.presentation.routes.settings.routes.notifications.NotificationsScreen
import com.example.domes.presentation.routes.settings.routes.notifications.NotificationsViewModel
import com.example.domes.presentation.routes.settings.routes.notifications.routes.ManagePrayerScreen
import com.example.domes.ui.theme.DomesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.IconSource
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedGetBackStackEntry")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
//        deleteDatabase("prayer_records")

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<PrayerTimesUpdateWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "periodicPrayerTimesUpdate", ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest
        )

        super.onCreate(savedInstanceState)
        setContent {
            DomesTheme {
                val locationPermissionState =
                    rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                val navController = rememberNavController()

//                val backStackEntry = remember {
//                    navController.getBackStackEntry("main")
//                }
//                val viewModel: PrayersViewModel = hiltViewModel(backStackEntry)
                val viewModel: PrayersViewModel =
                    hiltViewModel(LocalContext.current as ComponentActivity)
                val notificationsViewModel: NotificationsViewModel =
                    hiltViewModel(LocalContext.current as ComponentActivity)

                val isLocationKnown = true

                val settings = listOf(
                    Setting("Location",
                        IconSource(Icons.Outlined.LocationOn),
                        "Auto-update • set manually",
                        Screen.Settings.Location.route,
                        {
                            LocationScreen(
                                navController, prayersViewModel = viewModel
                            )
                        }),
                    Setting("Calculation",
                        IconSource(Icons.Outlined.Calculate),
                        "Method • juristic",
                        Screen.Settings.Calculation.route,
                        {
                            CalculationScreen(
                                navController, viewModel = viewModel
                            )
                        }),
                    Setting(
                        "Notifications",
                        IconSource(Icons.Outlined.Notifications),
                        "Adhan • prayers • corrections",
                        Screen.Settings.Notifications.route,
                        {
                            NotificationsScreen(
                                it,
                                viewModel = notificationsViewModel
                            )
                        },
                    ),
                    Setting("Integrations",
                        IconSource(Icons.Outlined.AccountTree),
                        "Calendar • Google Nest",
                        Screen.Settings.Integrations.route,
                        {
                            IntegrationsScreen(
                                navController
                            )
                        }),
                    Setting("Appearance",
                        IconSource(Icons.Outlined.Palette),
                        "Theme • language • format",
                        Screen.Settings.Appearance.route,
                        {
                            AppearanceScreen(
                                navController
                            )
                        }
                    ),
                    Setting(
                        "Widgets",
                        IconSource(Icons.Outlined.Widgets),
                        "Home screen widgets",
                        "widgets",
                        enabled = false
                    ),
                    Setting(
                        "Data",
                        IconSource(Icons.Outlined.FilePresent),
                        "Import • export",
                        "data",
                        enabled = false
                    ),
                    Setting(
                        "About",
                        IconSource(Icons.Outlined.Info),
                        "App version • contact",
                        "about",
                        category = Category.ABOUT,
                        enabled = false
                    ),
                    Setting(
                        "Privacy",
                        IconSource(Icons.Outlined.Security),
                        "Policy • terms",
                        "privacy",
                        category = Category.ABOUT,
                        enabled = false
                    ),
                    Setting(
                        "Feedback",
                        IconSource(Icons.Outlined.QuestionAnswer),
                        "Report a bug • suggest a feature",
                        "feedback",
                        category = Category.ABOUT,
                        enabled = false
                    ),
                )

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController,
                        startDestination = "main",
                        enterTransition = {
                            fadeIn(
                                animationSpec = tween(
                                    750,
                                    easing = EaseOutQuint
                                )
                            ) + slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(750, easing = EaseOutQuint)
                            ) { offsetForFullSlide ->
                                offsetForFullSlide / 8
                            }
                        },
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    750,
                                    easing = EaseOutQuint
                                )
                            ) + slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(750, easing = EaseOutQuint)
                            ) { offsetForFullSlide ->
                                offsetForFullSlide / 8
                            }
                        },
                        popEnterTransition = {
                            fadeIn(
                                animationSpec = tween(
                                    750,
                                    easing = EaseOutQuint
                                )
                            ) + slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(750, easing = EaseOutQuint)
                            ) { offsetForFullSlide ->
                                offsetForFullSlide / 8
                            }
                        },
                        popExitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    750,
                                    easing = EaseOutQuint
                                )
                            ) + slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(750, easing = EaseOutQuint)
                            ) { offsetForFullSlide ->
                                offsetForFullSlide / 8
                            }
                        }) {
                        navigation(
                            startDestination = if (isLocationKnown) Screen.Prayers.route else Screen.Settings.Location.route,
                            route = "main"
                        ) {
                            composable(route = Screen.Prayers.route) {
                                PrayersScreen(
                                    navController,
                                    viewModel = viewModel,
                                    notificationsViewModel = notificationsViewModel,
                                )
                            }
                            composable(route = Screen.Qibla.route) {
                                QiblaScreen(navController)
                            }
                            composable(route = Screen.Settings.route) {
                                SettingsScreen(navController, settings)
                            }
                            composable(route = Screen.Settings.Location.route) {
                                settings[0].screen(null)
                            }
                            composable(
                                route = Screen.Settings.Notifications.ManagePrayer.route + "?prayer={prayer}",
                                arguments = listOf(navArgument(name = "prayer") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                })
                            ) {
                                val prayer = Prayer.fromString(it.arguments?.getString("prayer"))

                                ManagePrayerScreen(prayer)
                            }
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
//
//    }
//}