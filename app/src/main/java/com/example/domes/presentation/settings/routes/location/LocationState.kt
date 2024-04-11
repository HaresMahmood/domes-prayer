package com.example.domes.presentation.settings.routes.location

import com.batoulapps.adhan2.Coordinates

data class LocationState(
    val isAutoUpdateEnabled: Boolean = true,
    val location: Coordinates? = null,
    val city: String = "",
)
