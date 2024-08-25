package com.example.domes.domain.usecase

import com.example.domes.domain.model.prayer.PrayerTime
import java.time.LocalDateTime

class GetCurrentPrayer {
    operator fun invoke(prayerTimes: List<PrayerTime>, currentTime: LocalDateTime): PrayerTime? {
        return prayerTimes.firstOrNull {
            currentTime.isAfter(it.time) && currentTime.isBefore(it.timeWindow)
        }
    }
}