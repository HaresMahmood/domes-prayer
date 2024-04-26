package com.example.domes.domain.usecase

import com.example.domes.data.repository.PrayerNotificationRepository
import com.example.domes.domain.model.prayer.PrayerNotification
import kotlinx.coroutines.flow.Flow

class GetPrayerNotifications(
    private val repository: PrayerNotificationRepository
) {
    operator fun invoke(): Flow<List<PrayerNotification>> {
        return repository.getAll()
    }
}