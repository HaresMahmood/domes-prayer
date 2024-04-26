package com.example.domes.domain.usecase

import com.example.domes.data.repository.PrayerNotificationRepository
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerNotification

class GetPrayerNotification(
    private val repository: PrayerNotificationRepository
) {
    suspend operator fun invoke(prayer: Prayer): PrayerNotification? {
        return repository.getByPrayer(prayer)
    }
}