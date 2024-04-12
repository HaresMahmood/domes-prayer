package com.example.domes.domain.usecase

import com.batoulapps.adhan2.Madhab
import com.example.domes.domain.repository.IUserPreferencesRepository

class GetJuristic(
    private val userPreferencesRepository: IUserPreferencesRepository,
) {
    suspend operator fun invoke(): Madhab? {
        return Madhab.entries.firstOrNull { it.ordinal == userPreferencesRepository.getJuristic() }
    }
}