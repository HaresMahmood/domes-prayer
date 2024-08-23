package com.example.domes.presentation.routes.prayers

import com.batoulapps.adhan2.PrayerTimes
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.model.prayer.PrayerTime
import java.time.Duration
import java.time.LocalDate
import java.time.chrono.HijrahDate

data class PrayersState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedHijriDate: HijrahDate = HijrahDate.now(),
    val isTodaySelected: Boolean = true,
    val currentRawPrayerTimes: PrayerTimes? = null,
    val currentPrayerTimes: List<PrayerTime> = emptyList(),
    val selectedPrayerTimes: List<PrayerTime> = emptyList(),
    var currentPrayer: PrayerTime? = null,
    val totalTime: Duration? = null,
    val timeLeft: Duration? = null,
    val dayFraction: Float = 0.0f,
    val snoozeTimer: Duration? = null,
    val expandedPrayer: PrayerTime? = null,
    val selectedPrayerRecord: PrayerRecord = PrayerRecord(LocalDate.now()),
    var isLoading: Boolean = true,
    var isRefreshing: Boolean = false,
    var showOverarchingDropdown: Boolean = false,
)
