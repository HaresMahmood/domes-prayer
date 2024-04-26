package com.example.domes.presentation.routes.settings.routes.notifications

import com.example.domes.domain.model.prayer.Prayer

sealed class NotificationsEvent {
    data class ChangeNotificationsAllowed(val isNotificationsAllowed: Boolean? = null): NotificationsEvent()
    data class ChangePrayerNotification(val prayer: Prayer, val isNotificationsAllowed: Boolean? = null): NotificationsEvent()
}
