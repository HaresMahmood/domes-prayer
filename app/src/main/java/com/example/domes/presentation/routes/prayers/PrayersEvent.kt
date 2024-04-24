package com.example.domes.presentation.routes.prayers

import com.example.domes.domain.model.prayer.MissedReason
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import java.time.LocalDate
import java.time.LocalDateTime

sealed class PrayersEvent {
    data class ChangeTime(val currentTime: LocalDateTime): PrayersEvent()
    data class ChangeCurrentPrayer(val currentTime: LocalDateTime): PrayersEvent()
    data class ChangePrayerRecord(val prayer: Prayer, val prayed: Boolean? = null, val notes: String? = null, val missedReason: MissedReason? = null): PrayersEvent()
    data class ChangeExpandedPrayer(val prayerTime: PrayerTime?): PrayersEvent()
    data class ChangeDate(val date: LocalDate): PrayersEvent()
    data object RefreshLocation : PrayersEvent()
    data class SetOverarchingDropdownVisibility(val isVisible: Boolean? = null): PrayersEvent()
}