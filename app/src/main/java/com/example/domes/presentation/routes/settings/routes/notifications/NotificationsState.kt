package com.example.domes.presentation.routes.settings.routes.notifications

import com.example.domes.domain.model.prayer.PrayerNotification

data class NotificationsState(
    val isNotificationsAllowed: Boolean = true,
    val prayerNotifications: List<PrayerNotification> = emptyList()
)
