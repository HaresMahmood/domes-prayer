package com.example.domes.data.repository

import com.example.domes.data.source.PrayerTimesDao
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import kotlinx.coroutines.flow.Flow

class PrayerTimesRepository(
    private val dao: PrayerTimesDao
) {
    fun getAll(): Flow<List<PrayerTime>> {
        return dao.getAll()
    }

    suspend fun getPrayer(prayer: Prayer): PrayerTime? {
        return dao.getPrayer(prayer)
    }

    suspend fun insert(prayerTime: PrayerTime) {
        dao.insert(prayerTime)
    }

    suspend fun delete(prayerTime: PrayerTime) {
        dao.delete(prayerTime)
    }

}