package com.example.domes.domain.usecase

import com.example.domes.data.repository.PrayerTimesRepository
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime

class GetPrayerTime(
    private val prayerTimesRepository: PrayerTimesRepository
) {
    suspend operator fun invoke(prayer: Prayer): PrayerTime? {
        return prayerTimesRepository.getByPrayer(prayer)
    }
}