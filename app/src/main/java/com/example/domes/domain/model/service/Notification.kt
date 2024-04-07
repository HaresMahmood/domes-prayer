package com.example.domes.domain.model.service

import android.content.Intent
import androidx.core.app.NotificationCompat.Action

data class Notification(
    val id: Int? = null,
    val title: String,
    val message: String,
    val mainAction: Intent? = null,
    val actions: List<Action>? = null
)
