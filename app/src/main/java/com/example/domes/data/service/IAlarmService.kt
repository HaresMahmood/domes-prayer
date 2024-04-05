package com.example.domes.data.service

import com.example.domes.domain.model.prayer.PrayerTime

interface IAlarmService {
    fun schedule(prayerTime: PrayerTime)
    fun cancel(prayerTime: PrayerTime)
}