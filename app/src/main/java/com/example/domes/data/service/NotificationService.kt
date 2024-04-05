package com.example.domes.data.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.domes.R
import com.example.domes.domain.model.service.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NotificationService(
    private val context: Context
) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(params: Notification) {
        CoroutineScope(Dispatchers.IO).launch {
            val notificationId = generateNotificationId()
            val notificationBuilder = context.createNotificationBuilder(params)
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    private fun generateNotificationId(): Int {
        val date = Date()
        return SimpleDateFormat("ddHHmmss", Locale.UK).format(date).toInt()
    }

    private fun createNotificationChannels() {
        val prayerChannel = NotificationChannel("prayer", "Prayer times", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Notifies the user at specific prayer times throughout the day"
        }

        notificationManager.createNotificationChannel(prayerChannel)
    }

    private fun Context.createNotificationBuilder(params: Notification): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, "prayer")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(params.title)
            .setContentText(params.message)
            .apply {
                params.actions?.forEach { action ->
                    addAction(action)
                }
            }
    }
}