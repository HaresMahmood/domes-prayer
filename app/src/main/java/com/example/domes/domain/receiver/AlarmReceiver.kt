package com.example.domes.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.domes.domain.service.NotificationService

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val prayer = intent?.getStringExtra("PRAYER") ?: return
        val time = intent.getStringExtra("TIME") ?: return

        println("NOTIFICATION: ${prayer} at ${time}")

        // If prayer isn't silent
        val notificationService = NotificationService(context)

        notificationService.sendNotification(
            NotificationService.NotificationParams(
                "Time for ${prayer}",
                "It's $"
            )
        )
    }
}