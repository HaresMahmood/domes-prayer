package com.example.domes.presentation.routes.settings.routes.appearance

sealed class AppearanceEvent {
    data object RefreshLocation : AppearanceEvent()
    data class ChangeCoordinates(val latitude: Double, val longitude: Double): AppearanceEvent()
    data object ToggleAutoUpdate : AppearanceEvent()
    data class SetCoordinatesDialogVisibility(val isVisible: Boolean? = null): AppearanceEvent()
}
