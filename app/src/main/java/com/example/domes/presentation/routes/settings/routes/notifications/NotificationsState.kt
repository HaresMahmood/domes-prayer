package com.example.domes.presentation.routes.settings.routes.notifications

import com.example.domes.domain.model.prayer.PrayerNotification

data class NotificationsState(
    val isNotificationsAllowed: Boolean = false,
    val prayerNotifications: List<PrayerNotification> = emptyList()
)
