package com.example.domes.domain.usecase

import com.batoulapps.adhan2.Madhab
import com.example.domes.domain.repository.IUserPreferencesRepository

class SetJuristic(
    private val userPreferencesRepository: IUserPreferencesRepository,
) {
    suspend operator fun invoke(juristic: Madhab) {
        userPreferencesRepository.setJuristic(juristic.ordinal)
    }
}