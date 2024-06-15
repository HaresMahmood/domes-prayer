package com.example.domes.presentation.routes.settings.routes.location

import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.model.location.Locale

data class LocationState(
    val isAutoUpdateEnabled: Boolean = false,
    val location: Coordinates? = null,
    val locale: Locale = Locale.unknown(),
    val showCoordinatesDialog: Boolean = false,
)