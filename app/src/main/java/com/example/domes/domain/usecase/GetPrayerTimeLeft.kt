package com.example.domes.domain.usecase

import com.example.domes.domain.model.PrayerTime
import java.time.Duration
import java.time.LocalDateTime

class GetPrayerTimeLeft {
    operator fun invoke(currentTime: LocalDateTime, nextPrayerTime: PrayerTime): Duration {
        return Duration.between(currentTime, nextPrayerTime.timeWindow)
    }
}