package com.example.domes.domain.service

import com.example.domes.domain.model.PrayerTime

interface IAlarmService {
    fun schedule(prayerTime: PrayerTime)
    fun cancel(prayerTime: PrayerTime)
}