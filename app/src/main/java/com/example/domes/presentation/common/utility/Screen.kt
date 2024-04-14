package com.example.domes.presentation.common.utility

sealed class Screen(val route: String) {
    data object Prayers: Screen("prayers")
    data object Settings: Screen("settings")
    data object Location: Screen("location")
    data object Calculation: Screen("calculation")
    data object Notifications: Screen("notifications")
    data object ManagePrayer: Screen("manage-prayer")
}