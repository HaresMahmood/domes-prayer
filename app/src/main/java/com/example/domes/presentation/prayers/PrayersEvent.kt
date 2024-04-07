package com.example.domes.presentation.prayers

import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import java.time.LocalDate
import java.time.LocalDateTime

sealed class PrayersEvent {
    data class ChangeTime(val currentTime: LocalDateTime): PrayersEvent()
    data class ChangeCurrentPrayer(val currentPrayer: PrayerTime): PrayersEvent()
    data class ChangePrayerRecord(val prayer: Prayer, val hasPrayed: Boolean): PrayersEvent()
    data class ChangeDate(val date: LocalDate): PrayersEvent()
    data class ChangeLocation(val coordinates: Coordinates): PrayersEvent()
}