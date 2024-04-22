package com.example.domes.domain.usecase

import com.example.domes.data.repository.PrayerTimesRepository
import com.example.domes.domain.model.prayer.PrayerTime

class UpdatePrayerTimes(
    private val prayerTimesRepository: PrayerTimesRepository
) {
    suspend operator fun invoke(prayerTimes: List<PrayerTime>) {
        prayerTimes.forEach { prayerTime ->
            prayerTimesRepository.insert(prayerTime)
        }
    }
}