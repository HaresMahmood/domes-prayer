package com.example.domes.presentation.routes.prayers

import android.app.Application
import android.app.NotificationManager
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
import com.example.domes.data.service.NotificationService.Companion.ACTION_NOTIFICATION_ACTION_CLICKED
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PrayersViewModel @Inject constructor(
    private val context: Application, private val prayersUseCases: PrayerUseCases
) : ViewModel() {
    private var getPrayerRecordJob: Job? = null

    private val _state = mutableStateOf(PrayersState())
    val state: State<PrayersState> = _state

    // Events

    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                val currentTime = LocalDateTime.now()

                onEvent(PrayersEvent.ChangeTime(currentTime))

                if (currentTime.isAfter(_state.value.currentPrayer?.timeWindow)) {
                    onEvent((PrayersEvent.ChangeCurrentPrayer(currentTime)))
                }
            }
        }
    }

    // Move to separate class to run in background
    private val notificationActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            intent?.getParcelableExtra<PrayerTime>("PRAYERTIME")?.let { prayerTime ->
                _state.value.selectedPrayerRecord.takeIf { it.date == prayerTime.time.toLocalDate() }
                    ?.let {
                        onEvent(PrayersEvent.ChangePrayerRecord(prayerTime.prayer, true))
                    } ?: viewModelScope.launch {
                    prayersUseCases.getPrayerRecord(prayerTime.time.toLocalDate())
                        ?.let { fetchedPrayerRecord ->
                            updatePrayerRecord(fetchedPrayerRecord, prayerTime.prayer, true)
                        }
                }

                // TODO: Move into use case and notification service
                val notificationId =
                    prayerTime.hashCode() // Use the same ID that was used to create the PendingIntent
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationId)
            }
        }
    }

    // Body

    init {
        context.registerReceiver(timeReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
        context.registerReceiver(notificationActionReceiver, IntentFilter(ACTION_NOTIFICATION_ACTION_CLICKED))

        viewModelScope.launch {
            val coordinates = prayersUseCases.getLocation()

            _state.value = _state.value.copy(
                location = coordinates,
                locale = prayersUseCases.getLocale(coordinates),
            )

            _state.value.location?.let { location ->
                updateCurrentPrayerTimes(location, _state.value.calculationParameters)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(timeReceiver)
        context.unregisterReceiver(notificationActionReceiver)
    }

    fun onEvent(event: PrayersEvent) {
        when (event) {
            is PrayersEvent.ChangeTime -> {
                _state.value = _state.value.copy(
                    timeLeft = _state.value.currentPrayer?.let {
                        prayersUseCases.getPrayerTimeLeft(event.currentTime, it)
                    },
                    dayFraction = _state.value.currentRawPrayerTimes?.let {
                        prayersUseCases.getDayFraction(it, LocalDateTime.now())
                    } ?: 0.0
                )
            }

            is PrayersEvent.ChangeCurrentPrayer -> {
                _state.value = _state.value.copy(
                    currentPrayer = prayersUseCases.getCurrentPrayer(
                        _state.value.currentPrayerTimes, event.currentTime
                    )
                )
            }

            is PrayersEvent.ChangeDate -> {
                _state.value = _state.value.copy(
                    selectedDate = event.date,
                    selectedHijriDate = prayersUseCases.getHijriDate(event.date)
                )

                getPrayerTimes(
                    _state.value.selectedDate,
                    _state.value.location!!,
                    _state.value.calculationParameters
                )
            }

            is PrayersEvent.RefreshLocation -> {
                _state.value.isRefreshing = true

                viewModelScope.launch {
                    val coordinates = prayersUseCases.fetchLocation()

                    _state.value = _state.value.copy(
                        location = coordinates, locale = prayersUseCases.getLocale(coordinates)
                    )
                }

                updateCurrentPrayerTimes(
                    _state.value.location!!, _state.value.calculationParameters
                )

                _state.value.isRefreshing = false
            }
            // TODO: Simplify
            is PrayersEvent.ChangePrayerRecord -> {
                val updatedPrayerRecord = updatePrayerRecord(
                    _state.value.selectedPrayerRecord, event.prayer, event.hasPrayed
                )

                _state.value = _state.value.copy(
                    selectedPrayerRecord = updatedPrayerRecord
                )
            }

            is PrayersEvent.SetOverarchingDropdownVisibility -> {
                _state.value = _state.value.copy(
                    showOverarchingDropdown = event.isVisible
                        ?: !_state.value.showOverarchingDropdown
                )
            }
        }
    }

    // TODO: move to use case?
    fun calculateRemainingPrayerTime() = _state.value.timeLeft?.let { timeLeft ->
        _state.value.totalTime?.let { totalTime ->
            timeLeft.toMillis().toDouble() / totalTime.toMillis().toDouble()
        }
    }?.toFloat() ?: 0.0f

    private fun mapPrayerToPrayerRecord(prayer: Prayer): Boolean {
        return when (prayer) {
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

    private fun updateCurrentPrayerTimes(
        coordinates: Coordinates, parameters: CalculationParameters
    ) {
        val currentTime = LocalDateTime.now()
        val (_, rawPrayerTimes) = prayersUseCases.getPrayerTimes(
            currentTime.toLocalDate(), coordinates, parameters
        )

        prayersUseCases.getCurrentPrayerTimes().onEach { prayerTimes ->
            val currentPrayer = prayersUseCases.getCurrentPrayer(prayerTimes, currentTime)

            _state.value = _state.value.copy(
                currentPrayerTimes = prayerTimes,
                currentRawPrayerTimes = rawPrayerTimes,
                currentPrayer = currentPrayer,
                timeLeft = prayersUseCases.getPrayerTimeLeft(currentTime, currentPrayer),
                totalTime = prayersUseCases.getPrayerTimeLeft(
                    currentPrayer.time, currentPrayer
                ),
                selectedHijriDate = prayersUseCases.getHijriDate(_state.value.selectedDate),
                dayFraction = prayersUseCases.getDayFraction(rawPrayerTimes, currentTime)
            )
        }.launchIn(viewModelScope).invokeOnCompletion {

            }
    }

    private fun getPrayerTimes(
        date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters
    ) {
        _state.value = _state.value.copy(
            selectedPrayerTimes = prayersUseCases.getPrayerTimes(
                date, coordinates, parameters
            ).first
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

    private fun updatePrayerRecord(
        prayerRecord: PrayerRecord, changedPrayer: Prayer, hasPrayed: Boolean
    ): PrayerRecord {
        val updatedPrayerRecord = prayerRecord.copy(
            fajr = if (changedPrayer == Prayer.FAJR) hasPrayed else prayerRecord.fajr,
            dhuhr = if (changedPrayer == Prayer.DHUHR) hasPrayed else prayerRecord.dhuhr,
            asr = if (changedPrayer == Prayer.ASR) hasPrayed else prayerRecord.asr,
            maghrib = if (changedPrayer == Prayer.MAGHRIB) hasPrayed else prayerRecord.maghrib,
            isha = if (changedPrayer == Prayer.ISHA) hasPrayed else prayerRecord.isha
        )

        viewModelScope.launch {
            prayersUseCases.addPrayerRecord(updatedPrayerRecord)
        }

        return updatedPrayerRecord
    }

    // private fun updatePrayerRecord(prayerRecord: PrayerRecord) {
    //     viewModelScope.launch {
    //         prayersUseCases.updatePrayerRecord(prayerRecord)
    //     }
    // }   
}