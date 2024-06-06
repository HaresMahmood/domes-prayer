package com.example.domes.presentation.routes.settings.routes.appearance

import com.batoulapps.adhan2.Coordinates

data class AppearanceState(
    val isAutoUpdateEnabled: Boolean = false,
    val location: Coordinates? = null,
    val locale: String = "",
    val showCoordinatesDialog: Boolean = false,
)