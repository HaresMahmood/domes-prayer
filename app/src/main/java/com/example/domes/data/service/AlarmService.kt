package com.example.domes.data.service

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.example.domes.data.receiver.AlarmReceiver
import com.example.domes.domain.model.prayer.Notification
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import java.time.ZoneId

class AlarmService(
    private val context: Application,
) : IAlarmService {
    companion object {
        const val ACTION_ALARM = "com.example.domes.ALARM"
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(prayerTime: PrayerTime, notification: Notification?) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("PRAYERTIME", prayerTime)
            putExtra("NOTIFICATION", notification)
            action = ACTION_ALARM
        }
        val exactPrayerTime = prayerTime.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        val offsetPrayerTime = exactPrayerTime + (notification?.offset ?: 0) * 60 * 1000

        println("SCHEDULE for ${prayerTime.prayer.name}: $exactPrayerTime, $offsetPrayerTime")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            offsetPrayerTime,
            PendingIntent.getBroadcast(
                context,
                prayerTime.prayer.hashCode() + notification.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(prayer: Prayer, notification: Notification?) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                prayer.hashCode() + notification.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}