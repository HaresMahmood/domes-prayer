package com.example.domes.domain.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.domes.domain.model.PrayerTime
import com.example.domes.domain.receiver.AlarmReceiver
import java.time.ZoneId

class AlarmService(
    private val context: Context
): IAlarmService {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
//    private val intent = Intent(context, AlarmReceiver::class.java)

    override fun schedule(prayerTime: PrayerTime) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("PRAYER", prayerTime.prayer.name)
            putExtra("TIME", prayerTime.time.toString())
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
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}