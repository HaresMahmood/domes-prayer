package com.example.domes.presentation.routes.settings.routes.integrations

sealed class IntegrationsEvent {
    data class ChangeExpandedSetting(val settingIndex: Int?) : IntegrationsEvent()
}
