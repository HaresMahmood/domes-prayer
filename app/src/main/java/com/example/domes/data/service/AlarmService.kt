package com.example.domes.data.service

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.example.domes.data.receiver.AlarmReceiver
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import java.time.ZoneId

class AlarmService(
    private val context: Application
): IAlarmService {
    companion object {
        const val ACTION_ALARM = "com.example.domes.ALARM"
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(prayerTime: PrayerTime) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("PRAYERTIME", prayerTime)
            action = ACTION_ALARM
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            prayerTime.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                prayerTime.prayer.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(prayer: Prayer) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                prayer.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}