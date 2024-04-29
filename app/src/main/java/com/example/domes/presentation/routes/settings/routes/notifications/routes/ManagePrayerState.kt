package com.example.domes.presentation.routes.settings.routes.notifications.routes

import com.example.domes.domain.model.prayer.PrayerNotification

data class ManagePrayerState(
    val expandedNotificationIndex: Int? = -1,
    val notification: PrayerNotification? = null
)
