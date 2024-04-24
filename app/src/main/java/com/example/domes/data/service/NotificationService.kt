package com.example.domes.data.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
    companion object {
        const val ACTION_NOTIFICATION_ACTION_CLICKED = "com.example.domes.ACTION_NOTIFICATION_ACTION_CLICKED"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(params: Notification) {
        CoroutineScope(Dispatchers.IO).launch {
            val notificationId = params.id ?: generateNotificationId()
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
        params.mainAction?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingAction = PendingIntent.getActivity(this, 0, params.mainAction,
            PendingIntent.FLAG_IMMUTABLE)


        return NotificationCompat.Builder(this, "prayer")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(params.title)
            .setContentText(params.message)
            .setContentIntent(mainPendingAction)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .apply {
                params.actions?.forEach { action ->
                    addAction(action)
                }
            }
    }
}