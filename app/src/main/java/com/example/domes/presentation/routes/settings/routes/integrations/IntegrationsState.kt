package com.example.domes.presentation.routes.settings.routes.integrations

import com.batoulapps.adhan2.Coordinates

data class IntegrationsState(
    val isAutoUpdateEnabled: Boolean = false,
    val location: Coordinates? = null,
    val locale: String = "",
    val showCoordinatesDialog: Boolean = false,
)