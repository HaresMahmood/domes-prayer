package com.example.domes.presentation.routes.settings.routes.location

import com.batoulapps.adhan2.Coordinates

sealed class LocationEvent {
    data object ToggleAutoUpdate : LocationEvent()
    data class ExpandCoordinatesSetting(val expanded: Boolean? = null) : LocationEvent()
    data class PrefillCoordinatesText(val coordinates: Coordinates) : LocationEvent()
    data object ResetCoordinatesText : LocationEvent()
    data class FetchEditedLocale(val coordinates: Coordinates) : LocationEvent()
    data class OnLatitudeTextChange(val text: String) : LocationEvent()
    data class OnLongitudeTextChange(val text: String) : LocationEvent()
}