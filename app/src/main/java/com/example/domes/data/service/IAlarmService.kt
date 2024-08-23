package com.example.domes.data.service

import com.example.domes.domain.model.prayer.Notification
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime

interface IAlarmService {
    fun schedule(prayerTime: PrayerTime, notification: Notification? = null)
    fun cancel(prayer: Prayer, notification: Notification? = null)
}