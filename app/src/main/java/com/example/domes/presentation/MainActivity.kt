package com.example.domes.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.domes.data.worker.PrayerTimesUpdateWorker
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.PrayersScreen
import com.example.domes.presentation.routes.qibla.QiblaScreen
import com.example.domes.presentation.routes.settings.SettingsScreen
import com.example.domes.presentation.routes.settings.routes.calculation.CalculationScreen
import com.example.domes.presentation.routes.settings.routes.location.LocationScreen
import com.example.domes.presentation.routes.settings.routes.notifications.NotificationsScreen
import com.example.domes.presentation.routes.settings.routes.notifications.routes.ManagePrayerScreen
import com.example.domes.ui.theme.DomesTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
//        deleteDatabase("prayer_records")

        // Immediate execution
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<PrayerTimesUpdateWorker>()
            .build()
        WorkManager.getInstance(this).enqueueUniqueWork(
            "oneTimePrayerTimesUpdate",
            ExistingWorkPolicy.KEEP,
            oneTimeWorkRequest
        )

        // Periodic execution
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<PrayerTimesUpdateWorker>(1, TimeUnit.DAYS)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "periodicPrayerTimesUpdate",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

        super.onCreate(savedInstanceState)
        setContent {
            DomesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Prayers.route,
                        enterTransition = {
                            fadeIn(animationSpec = tween(500, easing = EaseOutExpo)) +
                                    slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(500, easing = EaseOutExpo)
                                    ) { offsetForFullSlide ->
                                        offsetForFullSlide / 8
                                    }
                        },
                        exitTransition = {
                            fadeOut(animationSpec = tween(500, easing = EaseOutExpo)) +
                                    slideOutOfContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(500, easing = EaseOutExpo)
                                    ) { offsetForFullSlide ->
                                        offsetForFullSlide / 8
                                    }
                        },
                        popEnterTransition = {
                            fadeIn(animationSpec = tween(500, easing = EaseOutExpo)) +
                                    slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(500, easing = EaseOutExpo)
                                    ) { offsetForFullSlide ->
                                        offsetForFullSlide / 8
                                    }
                        },
                        popExitTransition = {
                            fadeOut(animationSpec = tween(500, easing = EaseOutExpo)) +
                                    slideOutOfContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(500, easing = EaseOutExpo)
                                    ) { offsetForFullSlide ->
                                        offsetForFullSlide / 8
                                    }
                        }
                    ) {
                        composable(route = Screen.Prayers.route) {
                            PrayersScreen(navController)
                        }
                        composable(route = Screen.Qibla.route) {
                            QiblaScreen(navController)
                        }
                        composable(route = Screen.Settings.route) {
                            SettingsScreen(navController)
                        }
                        composable(route = Screen.Location.route) {
                            LocationScreen(navController)
                        }
                        composable(route = Screen.Calculation.route) {
                            CalculationScreen(navController)
                        }
                        composable(route = Screen.Notifications.route) {
                            NotificationsScreen(navController)
                        }
                        composable(
                            route = Screen.ManagePrayer.route + "?prayer={prayer}",
                            arguments = listOf(
                                navArgument(name = "prayer") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) {
                            val prayer = Prayer.fromString(it.arguments?.getString("prayer"))

                            ManagePrayerScreen(navController, prayer)
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