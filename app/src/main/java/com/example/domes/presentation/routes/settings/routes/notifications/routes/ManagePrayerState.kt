package com.example.domes.presentation.routes.settings.routes.notifications.routes

import com.example.domes.domain.model.prayer.PrayerNotification

data class ManagePrayerState(
    val expandedNotificationIndex: Int? = null,
    val notification: PrayerNotification? = null
)
