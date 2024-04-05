package com.example.domes.domain.model.service

import androidx.core.app.NotificationCompat.Action

data class Notification(
    val title: String,
    val message: String,
    val actions: List<Action>? = null
)
