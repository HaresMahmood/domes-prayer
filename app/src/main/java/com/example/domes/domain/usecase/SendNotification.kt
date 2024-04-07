package com.example.domes.domain.usecase

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.domes.R
import com.example.domes.data.service.NotificationService
import com.example.domes.data.service.NotificationService.Companion.ACTION_NOTIFICATION_ACTION_CLICKED
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.domain.model.service.Notification
import com.example.domes.presentation.MainActivity

// TODO: Rename to something like SendPrayerNotification
class SendNotification(
    private val notificationService: NotificationService
) {
    operator fun invoke(prayerTime: PrayerTime, message: String, context: Context) {
        val mainIntent = Intent(context, MainActivity::class.java)
        val actions = prayerTime.prayer.displayable.takeIf { it }?.let {
            listOf(NotificationCompat.Action.Builder(
                R.drawable.ic_launcher_foreground,
                "Mark as completed",
                PendingIntent.getBroadcast(
                    context,
                    prayerTime.hashCode(),
                    Intent(ACTION_NOTIFICATION_ACTION_CLICKED).apply {
                    putExtra("PRAYERTIME", prayerTime)
                    action = ACTION_NOTIFICATION_ACTION_CLICKED
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build())
        } ?: emptyList()

        notificationService.sendNotification(
            Notification(
            id = prayerTime.hashCode(),
            title = prayerTime.prayer.name,
            message = message,
            mainAction = mainIntent,
            actions = actions
            )
        )
    }
}