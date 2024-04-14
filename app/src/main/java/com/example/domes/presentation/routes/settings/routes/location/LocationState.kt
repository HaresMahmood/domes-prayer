package com.example.domes.presentation.routes.settings.routes.location

import com.batoulapps.adhan2.Coordinates

data class LocationState(
    val isAutoUpdateEnabled: Boolean = false,
    val location: Coordinates? = null,
    val locale: String = "",
    val showCoordinatesDialog: Boolean = false,
)