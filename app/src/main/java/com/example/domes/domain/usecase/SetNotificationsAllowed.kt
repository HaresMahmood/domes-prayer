package com.example.domes.domain.usecase

import com.example.domes.domain.repository.IUserPreferencesRepository

class SetNotificationsAllowed(
    private val userPreferencesRepository: IUserPreferencesRepository,
) {
    suspend operator fun invoke(isNotificationsAllowed: Boolean) {
        userPreferencesRepository.setNotificationsAllowed(isNotificationsAllowed)
    }
}