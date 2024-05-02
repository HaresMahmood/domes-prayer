package com.example.domes.presentation.routes.settings.routes.notifications.routes

import com.example.domes.domain.model.prayer.PrayerNotification
import java.time.LocalDateTime

data class ManagePrayerState(
    val time: LocalDateTime = LocalDateTime.now(),
    val expandedNotificationIndex: Int? = 0,
    val notification: PrayerNotification? = null
)
