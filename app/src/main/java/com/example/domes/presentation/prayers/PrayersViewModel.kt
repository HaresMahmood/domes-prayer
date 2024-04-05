package com.example.domes.presentation.prayers

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.service.AlarmService
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PrayersViewModel @Inject constructor(
    private val context: Application,
    private val prayersUseCases: PrayerUseCases
) : ViewModel() {
    private val alarmService = AlarmService(context)

    private val _state = mutableStateOf(PrayersState())
    val state: State<PrayersState> = _state

    init {
        getPrayerTimes(_state.value.selectedDate, _state.value.location, _state.value.calculationParameters)
        onEvent(PrayersEvent.ChangeTime(LocalDateTime.now()))

        Log.d("PRAYER-VIEW-MODEL", "${_state.value.timeLeft}")
    }

    fun onEvent(event: PrayersEvent) {
        when(event) {
            is PrayersEvent.ChangeTime -> {
                _state.value = _state.value.copy(
                    timeLeft = _state.value.currentPrayer?.let {
                        prayersUseCases.getPrayerTimeLeft(event.currentTime, it)
                    }
                )
            }
            is PrayersEvent.ChangeDate -> {
                _state.value = _state.value.copy(
                    selectedDate = event.date,
                    selectedHijriDate = prayersUseCases.getHijriDate(event.date)
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
        _state.value.currentPrayerTimes.forEach {
            alarmService.cancel(it)
        }

        val prayerTimes = prayersUseCases.getPrayerTimes(date, coordinates, parameters)
        
        _state.value = _state.value.copy(
            currentPrayerTimes = prayerTimes,
            currentPrayer = prayerTimes.firstOrNull { it.isCurrent },
            nextPrayer = prayerTimes.firstOrNull { it.isNext }
        )

        prayerTimes.forEach {
            alarmService.schedule(it)
        }
    }
}