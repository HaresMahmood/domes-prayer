package com.example.domes.presentation.prayers

import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.model.Prayer
import java.time.LocalDate

sealed class PrayersEvent {
    data class CurrentPrayer(val prayer: Prayer): PrayersEvent()
    data class ChangeDate(val date: LocalDate): PrayersEvent()
    data class ChangeLocation(val coordinates: Coordinates): PrayersEvent()
}