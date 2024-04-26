package com.example.domes.presentation.routes.settings.routes.notifications.routes

import com.example.domes.domain.model.prayer.Notification

data class ManagePrayerState(
    val isNotificationAllowed: Boolean = true,
    val notifications: MutableList<Notification> = mutableListOf()
)
