package com.example.domes.presentation.prayers

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.example.domes.data.service.AlarmService
import com.example.domes.data.service.AlarmService.Companion.ALARM_ACTION
import com.example.domes.domain.model.service.Notification
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    // Events
    private val alarmReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val prayerName = intent?.getStringExtra("PRAYER")  ?: return

            println("NOTIFICATION: $prayerName")

            // If prayer isn't silent
            prayersUseCases.sendNotification(
                Notification(
                    "Time for ${prayerName}",
                    "Time to pray in ${_state.value.city}"
                )
            )
            
            onEvent(PrayersEvent.ChangeCurrentPrayer(_state.value.currentPrayerTimes.first { it.prayer.name == prayerName }))
        }
    }

    init {
        context.registerReceiver(alarmReceiver, IntentFilter(ALARM_ACTION))

        getPrayerTimes(_state.value.selectedDate, _state.value.location, _state.value.calculationParameters)
        onEvent(PrayersEvent.ChangeTime(LocalDateTime.now()))
        onEvent(PrayersEvent.ChangeLocation(_state.value.location))
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
            is PrayersEvent.ChangeCurrentPrayer -> {
                _state.value = _state.value.copy(
                    currentPrayer = event.currentPrayer
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
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        location = event.coordinates,
                        city = prayersUseCases.getCity(event.coordinates)
                    )
                }

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