package com.example.domes.data.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.domes.domain.model.prayer.PrayerTime
import java.time.ZoneId

class AlarmService(
    private val context: Context
): IAlarmService {
    companion object {
        const val ACTION_ALARM = "com.example.domes.ALARM"
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(prayerTime: PrayerTime) {
        val intent = Intent(ACTION_ALARM).apply {
            putExtra("PRAYER", prayerTime.prayer.name)
            action = ACTION_ALARM
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            prayerTime.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                prayerTime.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(prayerTime: PrayerTime) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                prayerTime.hashCode(),
                Intent(ACTION_ALARM),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}