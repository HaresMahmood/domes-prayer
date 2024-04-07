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
import com.example.domes.data.service.AlarmService.Companion.ACTION_ALARM
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.domain.model.service.Notification
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PrayersViewModel @Inject constructor(
    private val context: Application,
    private val prayersUseCases: PrayerUseCases
) : ViewModel() {
    private val alarmService = AlarmService(context) // TODO: Make singleton
    private var getPrayerRecordJob: Job? = null

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
                    prayerName,
                    _state.value.city,
                )
            )
            
            onEvent(PrayersEvent.ChangeCurrentPrayer(_state.value.currentPrayerTimes.first { it.prayer.name == prayerName }))
        }
    }

    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                println("TICK TACK MR WICK")
                onEvent(PrayersEvent.ChangeTime(LocalDateTime.now()))
            }
        }
    }

    // Body

    init {
        context.registerReceiver(timeReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
        context.registerReceiver(alarmReceiver, IntentFilter(ACTION_ALARM))
        updateCurrentPrayerTimes(state.value.location, _state.value.calculationParameters)

        viewModelScope.launch {
            _state.value = _state.value.copy(
                city = prayersUseCases.getCity(_state.value.location),
                selectedHijriDate = prayersUseCases.getHijriDate(_state.value.selectedDate),
                timeLeft = _state.value.currentPrayer?.let {
                    prayersUseCases.getPrayerTimeLeft(LocalDateTime.now(), it)
                },
                dayFraction = prayersUseCases.getDayFraction(_state.value.currentRawPrayerTimes!!, LocalDateTime.now())
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(timeReceiver)
        context.unregisterReceiver(timeReceiver)
    }

    fun onEvent(event: PrayersEvent) {
        when(event) {
            is PrayersEvent.ChangeTime -> {
                _state.value = _state.value.copy(
                    timeLeft = _state.value.currentPrayer?.let {
                        prayersUseCases.getPrayerTimeLeft(event.currentTime, it)
                    },
                    dayFraction = prayersUseCases.getDayFraction(_state.value.currentRawPrayerTimes!!, LocalDateTime.now())
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

                updateCurrentPrayerTimes(_state.value.location, _state.value.calculationParameters)
            }
            // TODO: Simplify
            is PrayersEvent.ChangePrayerRecord -> {
                val changedPrayer = event.prayer
                val hasPrayed = event.hasPrayed

                val prayerRecord = _state.value.selectedPrayerRecord.copy(
                    fajr = if (changedPrayer == Prayer.FAJR) hasPrayed else _state.value.selectedPrayerRecord.fajr,
                    dhuhr = if (changedPrayer == Prayer.DHUHR) hasPrayed else _state.value.selectedPrayerRecord.dhuhr,
                    asr = if (changedPrayer == Prayer.ASR) hasPrayed else _state.value.selectedPrayerRecord.asr,
                    maghrib = if (changedPrayer == Prayer.MAGHRIB) hasPrayed else _state.value.selectedPrayerRecord.maghrib,
                    isha = if (changedPrayer == Prayer.ISHA) hasPrayed else _state.value.selectedPrayerRecord.isha
                )

                viewModelScope.launch {
                    prayersUseCases.addPrayerRecord(prayerRecord)
                }

                _state.value = _state.value.copy(
                    selectedPrayerRecord = prayerRecord
                )
            }
        }
    }

    // TODO: move to use case
    fun calculateRemainingPrayerTime() = _state.value.timeLeft?.let { timeLeft ->
        _state.value.totalTime?.let { totalTime ->
            timeLeft.toMillis().toDouble() / totalTime.toMillis().toDouble()
        }
    }?.toFloat() ?: 0.0f

    fun mapPrayerToPrayerRecord(prayer: Prayer): Boolean {
        return when(prayer) {
            Prayer.FAJR -> _state.value.selectedPrayerRecord.fajr
            Prayer.DHUHR -> _state.value.selectedPrayerRecord.dhuhr
            Prayer.ASR -> _state.value.selectedPrayerRecord.asr
            Prayer.MAGHRIB -> _state.value.selectedPrayerRecord.maghrib
            Prayer.ISHA -> _state.value.selectedPrayerRecord.isha
            else -> false
        }
    }

    fun mapPrayerToPrayerRecord(prayerTime: PrayerTime): Boolean {
        return mapPrayerToPrayerRecord(prayerTime.prayer)
    }

    private fun updateCurrentPrayerTimes(coordinates: Coordinates, parameters: CalculationParameters) {
        _state.value.currentPrayerTimes.forEach(alarmService::cancel)
        val currentTime = LocalDateTime.now()
        val (prayerTimes, rawPrayerTimes) = prayersUseCases.getPrayerTimes(
            currentTime.toLocalDate(),
            coordinates,
            parameters
        )
        val currentPrayer = prayersUseCases.getCurrentPrayer(prayerTimes, currentTime)

        _state.value = _state.value.copy(
            currentPrayerTimes = prayerTimes.onEach(alarmService::schedule),
            currentRawPrayerTimes = rawPrayerTimes,
            currentPrayer = currentPrayer,
            timeLeft = prayersUseCases.getPrayerTimeLeft(currentTime, currentPrayer),
            totalTime = prayersUseCases.getPrayerTimeLeft(currentPrayer.time, currentPrayer),
        )

        getPrayerRecord(currentTime.toLocalDate())
    }

    private fun getPrayerTimes(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters) {
        _state.value = _state.value.copy(
            selectedPrayerTimes = prayersUseCases.getPrayerTimes(date, coordinates, parameters).first
        )

        getPrayerRecord(date)
    }

    private fun getPrayerRecord(date: LocalDate) {
        getPrayerRecordJob?.cancel()

        getPrayerRecordJob = viewModelScope.launch {
            prayersUseCases.getPrayerRecord(date).let {
                _state.value = _state.value.copy(
                    selectedPrayerRecord = it ?: PrayerRecord(date)
                )
            }
        }
    }

    // private fun updatePrayerRecord(prayerRecord: PrayerRecord) {
    //     viewModelScope.launch {
    //         prayersUseCases.updatePrayerRecord(prayerRecord)
    //     }
    // }   
}