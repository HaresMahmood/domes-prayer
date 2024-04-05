package com.example.domes.data.repository

import com.example.domes.data.source.PrayerRecordDao
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.repository.IPrayerRecordRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class PrayerRecordRepository(
    private val dao: PrayerRecordDao
) : IPrayerRecordRepository {
    override fun getAll(): Flow<List<PrayerRecord>> {
        return dao.getAll()
    }

    override suspend fun getByDate(date: LocalDate): PrayerRecord? {
        return dao.getByDate(date)
    }

    override suspend fun insert(record: PrayerRecord) {
        dao.insert(record)
    }

    override suspend fun delete(record: PrayerRecord) {
        dao.delete(record)
    }

}