package com.example.domes.domain.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.domes.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NotificationService(
    private val context: Context
) {
    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    data class NotificationParams(
        val title: String,
        val message: String,
        val image: Bitmap? = null,
        val actions: List<NotificationCompat.Action>? = null
    )

    init {
        createNotificationChannels()
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(params: NotificationParams) {
        Log.d(params.title, params.message)

        val date = Date()
        val notificationId = SimpleDateFormat("ddHHmmss", Locale.UK).format(date).toInt()
        val notificationBuilder = NotificationCompat.Builder(context, "Trigger")
            .setSmallIcon( R.drawable.ic_launcher_foreground)
            .setContentTitle(params.title)
            .setContentText(params.message)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(params.image)
//                .bigLargeIcon(null)
            )

        params.actions?.forEach { action ->
            notificationBuilder.addAction(action)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val persistentChannel = NotificationChannel("Persistent notification", "Persistent Notification", NotificationManager.IMPORTANCE_MIN).apply {
                description = "Notifies the user of the current context"
            }
            val triggerChannel = NotificationChannel("Trigger", "Trigger", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Notifies the user of a trigger"
            }

            notificationManager.createNotificationChannel(persistentChannel)
            notificationManager.createNotificationChannel(triggerChannel)
        }
    }

    companion object {
//        var instance: NotificationService? = null
//            private set
//            get() {
//                return field ?: throw IllegalStateException("Notification service not initialized")
//            }
    }
}