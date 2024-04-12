package com.example.domes.domain.usecase

import com.batoulapps.adhan2.CalculationMethod
import com.example.domes.domain.repository.IUserPreferencesRepository

class SetCalculationMethod(
    private val userPreferencesRepository: IUserPreferencesRepository,
) {
    suspend operator fun invoke(calculationMethod: CalculationMethod) {
        userPreferencesRepository.setCalculationMethod(calculationMethod.ordinal)
    }
}