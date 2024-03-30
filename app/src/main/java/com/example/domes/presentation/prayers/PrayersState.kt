package com.example.domes.presentation.prayers

import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.example.domes.domain.model.PrayerRecord
import java.time.LocalDate

data class PrayersState(
    val selectedDate: LocalDate = LocalDate.now(),
    val location: Coordinates? = Coordinates(55.825313, -4.293643),
    val prayerTimes: PrayerTimes? = null,
    val calculationParameters: CalculationParameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters,
    val prayerRecords: List<PrayerRecord> = emptyList()
)
