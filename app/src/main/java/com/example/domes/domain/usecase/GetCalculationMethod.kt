package com.example.domes.domain.usecase

import com.batoulapps.adhan2.CalculationMethod
import com.example.domes.domain.repository.IUserPreferencesRepository

class GetCalculationMethod(
    private val userPreferencesRepository: IUserPreferencesRepository,
) {
    suspend operator fun invoke(): CalculationMethod? {
        return CalculationMethod.entries.firstOrNull { it.ordinal == userPreferencesRepository.getCalculationMethod() }
    }
}