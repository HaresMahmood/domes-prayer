package com.example.domes.domain.usecase

import com.example.domes.data.repository.PrayerTimesRepository
import com.example.domes.domain.model.prayer.PrayerTime
import kotlinx.coroutines.flow.Flow

class GetCurrentPrayerTimes(
    private val prayerTimesRepository: PrayerTimesRepository
) {
    operator fun invoke(): Flow<List<PrayerTime>> {
        return prayerTimesRepository.getAll()
    }
}