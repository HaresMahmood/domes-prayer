package com.example.domes.domain.repository

import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import java.time.LocalDate

interface IPrayerRepository {
    suspend fun getBy(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters): PrayerTimes?
}