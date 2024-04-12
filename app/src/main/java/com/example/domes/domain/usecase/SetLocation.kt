package com.example.domes.domain.usecase

import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.repository.IUserPreferencesRepository

class SetLocation(
    private val userPreferencesRepository: IUserPreferencesRepository,
) {
    suspend operator fun invoke(coordinates: Coordinates) {
        userPreferencesRepository.setLocation(coordinates.latitude, coordinates.longitude)
    }
}