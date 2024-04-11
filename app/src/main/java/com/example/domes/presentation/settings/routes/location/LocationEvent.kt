package com.example.domes.presentation.settings.routes.location

sealed class LocationEvent {
    class RefreshLocation: LocationEvent()
    class ToggleAutoUpdate: LocationEvent()
}
