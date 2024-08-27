package com.example.domes.presentation.routes.settings.routes.location

sealed class LocationEvent {
    data object ToggleAutoUpdate : LocationEvent()
    data class OnLatitudeTextChange(val text: String) : LocationEvent()
    data class OnLongitudeTextChange(val text: String) : LocationEvent()
    data class ExpandCoordinatesSetting(val expanded: Boolean? = null) : LocationEvent()
}