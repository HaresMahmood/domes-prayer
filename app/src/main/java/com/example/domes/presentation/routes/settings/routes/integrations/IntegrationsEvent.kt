package com.example.domes.presentation.routes.settings.routes.integrations

sealed class IntegrationsEvent {
    data object RefreshLocation : IntegrationsEvent()
    data class ChangeCoordinates(val latitude: Double, val longitude: Double): IntegrationsEvent()
    data object ToggleAutoUpdate : IntegrationsEvent()
    data class SetCoordinatesDialogVisibility(val isVisible: Boolean? = null): IntegrationsEvent()
}
