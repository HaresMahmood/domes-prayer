package com.example.domes.presentation.routes.settings.routes.location

import com.example.domes.domain.model.location.Locale

data class LocationState(
    val isAutoUpdateEnabled: Boolean = false,
    val latitudeText: String = "",
    val longitudeText: String = "",
    val expandCoordinatesSetting: Boolean = false,
    val editedLocale: Locale? = null,
    val showLatitudeTextError: Boolean = false,
    val showLongitudeTextError: Boolean = false,
)