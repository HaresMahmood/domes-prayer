package com.example.domes.presentation.routes.settings.routes.location

data class LocationState(
    val isAutoUpdateEnabled: Boolean = false,
    val latitudeText: String = "",
    val longitudeText: String = "",
    val expandCoordinatesSetting: Boolean = false,
)