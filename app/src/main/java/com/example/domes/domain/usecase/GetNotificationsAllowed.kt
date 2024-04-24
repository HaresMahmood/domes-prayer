package com.example.domes.domain.usecase

import com.example.domes.domain.repository.IUserPreferencesRepository

class GetNotificationsAllowed(
    private val userPreferencesRepository: IUserPreferencesRepository
) {
    suspend operator fun invoke(): Boolean {
        return userPreferencesRepository.getNotificationsAllowed() ?: true
    }
}