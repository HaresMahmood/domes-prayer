package com.example.domes.domain.usecase

import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.repository.IUserPreferencesRepository

class GetLocation(
    private val userPreferencesRepository: IUserPreferencesRepository,
) {
    suspend operator fun invoke(): Coordinates? {
        return userPreferencesRepository.getLocation()
    }
}