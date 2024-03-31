package com.example.domes.presentation.prayers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PrayersViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases
) : ViewModel() {
    private val _state = mutableStateOf(PrayersState())
    val state: State<PrayersState> = _state

    init {
        getPrayerTimes(_state.value.selectedDate, _state.value.location, _state.value.calculationParameters)
    }

    fun onEvent(event: PrayersEvent) {
        when(event) {
            is PrayersEvent.ChangeDate -> {
                _state.value = _state.value.copy(
                    selectedDate = event.date
                )

                getPrayerTimes(_state.value.selectedDate, _state.value.location, _state.value.calculationParameters)
            }
            is PrayersEvent.ChangeLocation -> {
                _state.value = _state.value.copy(
                    location = event.coordinates
                )

                getPrayerTimes(_state.value.selectedDate, _state.value.location, _state.value.calculationParameters)
            }
        }
    }

    private fun getPrayerTimes(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters) {
        _state.value = _state.value.copy(
            prayerTimes = prayersUseCases.getPrayerTimes(date, coordinates, parameters)
        )
    }
}