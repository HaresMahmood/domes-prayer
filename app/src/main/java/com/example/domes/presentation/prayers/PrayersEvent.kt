package com.example.domes.presentation.prayers

import com.batoulapps.adhan2.Coordinates
import java.time.LocalDate
import java.time.LocalDateTime

sealed class PrayersEvent {
    data class ChangeTime(val currentTime: LocalDateTime): PrayersEvent()
    data class ChangeDate(val date: LocalDate): PrayersEvent()
    data class ChangeLocation(val coordinates: Coordinates): PrayersEvent()
}