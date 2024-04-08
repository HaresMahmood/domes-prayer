package com.example.domes.domain.usecase

import com.example.domes.domain.model.prayer.PrayerTime
import java.time.Duration
import java.time.LocalDateTime

class GetPrayerTimeLeft {
    operator fun invoke(currentTime: LocalDateTime, prayerTime: PrayerTime): Duration {
        return Duration.between(currentTime, prayerTime.timeWindow)
    }
}