package com.example.domes.presentation.prayers

import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.model.prayer.PrayerTime
import java.time.Duration
import java.time.LocalDate
import java.time.chrono.HijrahDate

data class PrayersState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedHijriDate: HijrahDate = HijrahDate.now(),
    val location: Coordinates = Coordinates(55.825313, -4.293643),
    val city: String = "",
    val currentRawPrayerTimes: PrayerTimes? = null,
    val currentPrayerTimes: List<PrayerTime> = emptyList(),
    val selectedPrayerTimes: List<PrayerTime> = emptyList(),
    var currentPrayer: PrayerTime? = null,
    val totalTime: Duration? = null,
    val timeLeft: Duration? = null,
    val dayFraction: Double = 0.0,
    val calculationParameters: CalculationParameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters,
    val prayerRecords: List<PrayerRecord> = emptyList()
)
