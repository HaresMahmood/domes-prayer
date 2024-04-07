package com.example.domes.domain.usecase

import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.repository.IPrayerRecordRepository

class AddPrayerRecord(
    private val repository: IPrayerRecordRepository
) {
    suspend operator fun invoke(prayerRecord: PrayerRecord) {
        return repository.insert(prayerRecord)
    }
}