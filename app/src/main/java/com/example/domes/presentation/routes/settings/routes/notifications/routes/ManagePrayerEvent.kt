package com.example.domes.presentation.routes.settings.routes.notifications.routes

sealed class ManagePrayerEvent {
    data class ChangeNotificationsAllowed(val isNotificationAllowed: Boolean? = null): ManagePrayerEvent()
}
