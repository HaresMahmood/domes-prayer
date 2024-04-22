package com.example.domes.presentation.routes.prayers

import com.example.domes.domain.model.prayer.Prayer
import java.time.LocalDate
import java.time.LocalDateTime

sealed class PrayersEvent {
    data class ChangeTime(val currentTime: LocalDateTime): PrayersEvent()
    data class ChangeCurrentPrayer(val currentTime: LocalDateTime): PrayersEvent()
    data class ChangePrayerRecord(val prayer: Prayer, val hasPrayed: Boolean): PrayersEvent()
    data class ChangeDate(val date: LocalDate): PrayersEvent()
    data object RefreshLocation : PrayersEvent()
    data class SetOverarchingDropdownVisibility(val isVisible: Boolean? = null): PrayersEvent()
}