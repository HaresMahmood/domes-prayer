package com.example.domes.data.repository

import com.example.domes.data.source.PrayerNotificationDao
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerNotification
import kotlinx.coroutines.flow.Flow

class PrayerNotificationRepository(
    private val dao: PrayerNotificationDao
) {
    fun getAll(): Flow<List<PrayerNotification>> {
        return dao.getAll()
    }

    suspend fun getByPrayer(prayer: Prayer): PrayerNotification? {
        return dao.getByPrayer(prayer)
    }

    suspend fun insert(notification: PrayerNotification) {
        dao.insert(notification)
    }

    suspend fun delete(notification: PrayerNotification) {
        dao.delete(notification)
    }

}