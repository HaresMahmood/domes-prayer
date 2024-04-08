package com.example.domes.presentation.common.utility

sealed class Screen(val route: String) {
    object Prayers: Screen("prayers")
    object Settings: Screen("settings")
    object Location: Screen("location")
}