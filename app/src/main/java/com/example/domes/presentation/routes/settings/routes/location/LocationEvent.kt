package com.example.domes.presentation.routes.settings.routes.location

sealed class LocationEvent {
    data object RefreshLocation : LocationEvent()
    data class ChangeCoordinates(val latitude: Double, val longitude: Double): LocationEvent()
    data object ToggleAutoUpdate : LocationEvent()
    data class SetCoordinatesDialogVisibility(val isVisible: Boolean? = null): LocationEvent()
}
