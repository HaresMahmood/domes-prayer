package com.example.domes.domain.usecase

import com.example.domes.data.repository.PrayerNotificationRepository
import com.example.domes.domain.model.prayer.PrayerNotification

class AddPrayerNotification(
    private val repository: PrayerNotificationRepository
) {
    suspend operator fun invoke(notification: PrayerNotification) {
        return repository.insert(notification)
    }
}