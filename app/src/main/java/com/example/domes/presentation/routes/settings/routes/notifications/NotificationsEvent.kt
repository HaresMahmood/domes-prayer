package com.example.domes.presentation.routes.settings.routes.notifications

sealed class NotificationsEvent {
    data class ChangeNotificationsAllowed(val isNotificationsAllowed: Boolean? = null): NotificationsEvent()
}
