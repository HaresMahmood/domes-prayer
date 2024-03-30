package com.example.domes.domain.usecase

import com.example.domes.domain.model.PrayerRecord
import com.example.domes.domain.repository.IPrayerRecordRepository
import java.time.LocalDate

class GetPrayerRecord(
    private val repository: IPrayerRecordRepository
) {
    suspend operator fun invoke(date: LocalDate): PrayerRecord? {
        return repository.getByDate(date)
    }
}