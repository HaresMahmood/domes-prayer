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
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Madhab
import com.example.domes.data.service.NotificationService.Companion.ACTION_NOTIFICATION_ACTION_CLICKED
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.presentation.routes.settings.routes.calculation.CalculationEvent
import com.example.domes.presentation.routes.settings.routes.calculation.CalculationState
import com.example.domes.presentation.routes.settings.routes.location.LocationEvent
import com.example.domes.presentation.routes.settings.routes.location.LocationState
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
    private val context: Application,
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(PrayersState())
    private val _locationState = mutableStateOf(LocationState())
    private val _calculationState = mutableStateOf(CalculationState())

    val prayersState: State<PrayersState> = _state
    val locationState: State<LocationState> = _locationState
    val calculationState: State<CalculationState> = _calculationState

    private var getPrayerRecordJob: Job? = null
    private val currentTime = LocalDateTime.now()

    // Events

    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                onEvent(PrayersEvent.ChangeTime(currentTime))

                if (currentTime.isAfter(_state.value.currentPrayer?.timeWindow)) {
                    onEvent((PrayersEvent.ChangeCurrentPrayer(currentTime)))
                }
            }
        }
    }

    // TODO: Move to separate class to run in background
    private val notificationActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            intent?.getParcelableExtra<PrayerTime>("PRAYERTIME")?.let { prayerTime ->
                _state.value.selectedPrayerRecord.takeIf { it.date == prayerTime.time.toLocalDate() }
                    ?.let {
                        onEvent(PrayersEvent.ChangePrayerRecord(prayerTime.prayer, true))
                    } ?: viewModelScope.launch {
                    prayersUseCases.getPrayerRecord(prayerTime.time.toLocalDate())?.details?.find { it.prayer == prayerTime.prayer }?.prayed =
                        true
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
        context.registerReceiver(
            notificationActionReceiver, IntentFilter(ACTION_NOTIFICATION_ACTION_CLICKED)
        )
        viewModelScope.launch {
            _state.value.isLoading = true

            getLocation { location ->
                updateCurrentPrayerTimes(
                    location,
                    calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                )
            }

            _state.value.isLoading = false
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
                _state.value = _state.value.copy(timeLeft = _state.value.currentPrayer?.let {
                    prayersUseCases.getPrayerTimeLeft(event.currentTime, it)
                }, dayFraction = _state.value.currentRawPrayerTimes?.let {
                    prayersUseCases.getDayFraction(it, LocalDateTime.now())
                } ?: 0.0f)
            }

            is PrayersEvent.ChangeCurrentPrayer -> {
                _state.value = _state.value.copy(
                    currentPrayer = prayersUseCases.getCurrentPrayer(
                        _state.value.currentPrayerTimes, event.currentTime
                    )
                )
            }

            is PrayersEvent.ChangeExpandedPrayer -> {
                _state.value = _state.value.copy(
                    expandedPrayer = event.prayerTime
                )
            }

            is PrayersEvent.ChangeDate -> {
                val isTodaySelected = event.date == currentTime.toLocalDate()
                _state.value = _state.value.copy(selectedDate = event.date,
                    selectedHijriDate = prayersUseCases.getHijriDate(event.date),
                    isTodaySelected = isTodaySelected,
                    dayFraction = _state.value.currentRawPrayerTimes?.let { prayers ->
                        if (isTodaySelected) prayersUseCases.getDayFraction(prayers, currentTime)
                        else if (event.date.isAfter(currentTime.toLocalDate())) 0.0f
                        else 1.0f
                    } ?: 0.0f)

                getPrayerTimes(
                    _state.value.selectedDate,
                    _locationState.value.location!!,
                    _calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                )
            }

            is PrayersEvent.RefreshLocation -> {
//                _state.value.isRefreshing = true
//                _state.value.isLoading = true
//
//                viewModelScope.launch {
//                    val coordinates = prayersUseCases.fetchLocation()
//
//                    _state.value = _state.value.copy(
//                        location = coordinates, locale = prayersUseCases.getLocale(coordinates)
//                    )
//                }
//
//                updateCurrentPrayerTimes(
//                    _state.value.location!!, _state.value.calculationParameters
//                )
//
//                _state.value.isRefreshing = false
//                _state.value.isLoading = false
            }

            is PrayersEvent.ChangePrayerRecord -> {
                // TODO: Creating a map, really?
                val updatedDetails = _state.value.selectedPrayerRecord.details.map { detail ->
                    if (detail.prayer == event.prayer) {
                        detail.copy(
                            prayed = event.prayed ?: detail.prayed,
                            notes = event.notes ?: detail.notes,
                            missedReason = event.missedReason
                        )
                    } else {
                        detail
                    }
                }
                val updatedPrayerRecord =
                    _state.value.selectedPrayerRecord.copy(details = updatedDetails)

                _state.value = _state.value.copy(selectedPrayerRecord = updatedPrayerRecord)

                viewModelScope.launch {
                    prayersUseCases.addPrayerRecord(updatedPrayerRecord)
                }
            }

            is PrayersEvent.SetOverarchingDropdownVisibility -> {
                _state.value = _state.value.copy(
                    showOverarchingDropdown = event.isVisible
                        ?: !_state.value.showOverarchingDropdown
                )
            }
        }
    }

    fun onEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.RefreshLocation -> {
                viewModelScope.launch {
                    fetchLocation { location ->
                        updateCurrentPrayerTimes(
                            location,
                            calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                        )
                    }
                }
            }

            // TODO: Broken?
            is LocationEvent.ChangeCoordinates -> {
                val coordinates = Coordinates(event.latitude, event.longitude)

                println("changing location coordinates...")
                println("location 1: ${_locationState.value.location?.latitude}, ${_locationState.value.location?.longitude}")

                viewModelScope.launch {
                    _locationState.value = _locationState.value.copy(
                        location = coordinates,
                        locale = prayersUseCases.getLocale(coordinates)
                    )

                    updateCurrentPrayerTimes(
                        coordinates,
                        calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                    )

                    prayersUseCases.setLocation(coordinates)

                    println("location coordinates: ${coordinates.latitude}, ${coordinates.longitude}")
                    println("location 2: ${_locationState.value.location?.latitude}, ${_locationState.value.location?.longitude}")
                }
            }

            is LocationEvent.ToggleAutoUpdate -> {
                _locationState.value = _locationState.value.copy(
                    isAutoUpdateEnabled = !_locationState.value.isAutoUpdateEnabled
                )
            }

            // TODO: Broken?
            is LocationEvent.SetCoordinatesDialogVisibility -> {
                println("set coordinates dialog visibility: ${event.isVisible}")

                _locationState.value = _locationState.value.copy(
                    showCoordinatesDialog = event.isVisible
                        ?: !_locationState.value.showCoordinatesDialog
                )
            }
        }
    }

    fun onEvent(event: CalculationEvent) {
        when (event) {
            is CalculationEvent.ChangeCalculationMethod -> {
                _calculationState.value = _calculationState.value.copy(
                    calculationMethod = event.calculationMethod
                )

                updateCurrentPrayerTimes(
                    _locationState.value.location!!,
                    event.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                )

                viewModelScope.launch {
                    prayersUseCases.setCalculationMethod(event.calculationMethod)
                }
            }

            is CalculationEvent.ChangeJuristic -> {
                _calculationState.value = _calculationState.value.copy(
                    juristic = event.juristic
                )

                updateCurrentPrayerTimes(
                    _locationState.value.location!!,
                    _calculationState.value.calculationMethod.parameters.copy(madhab = event.juristic)
                )

                viewModelScope.launch {
                    prayersUseCases.setJuristic(event.juristic)
                }
            }

            is CalculationEvent.ResetSettings -> {
                viewModelScope.launch {
                    _calculationState.value = _calculationState.value.copy(
                        calculationMethod = CalculationMethod.MUSLIM_WORLD_LEAGUE,
                        juristic = Madhab.SHAFI,
                    )

                    prayersUseCases.setCalculationMethod(_calculationState.value.calculationMethod)
                    prayersUseCases.setJuristic(_calculationState.value.juristic)
                }
            }

            is CalculationEvent.SetCalculationMethodDialogVisibility -> {
                _calculationState.value = _calculationState.value.copy(
                    showCalculationMethodDialog = event.isVisible
                        ?: !_calculationState.value.showCalculationMethodDialog
                )
            }

            is CalculationEvent.SetJuristicDialogVisibility -> {
                _calculationState.value = _calculationState.value.copy(
                    showJuristicDialog = event.isVisible
                        ?: !_calculationState.value.showJuristicDialog
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

    private fun updateCurrentPrayerTimes(
        coordinates: Coordinates, parameters: CalculationParameters,
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

            getPrayerRecord(currentTime.toLocalDate())
        }.launchIn(viewModelScope)
    }

    private fun getPrayerTimes(
        date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters,
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

    private suspend fun getLocation(
        onDone: (Coordinates) -> Unit = {},
    ) = prayersUseCases.getLocation()?.let {
        _locationState.value = _locationState.value.copy(
            location = it, locale = prayersUseCases.getLocale(it)
        )

        onDone(it)
    }

    private suspend fun fetchLocation(
        onDone: (Coordinates) -> Unit,
    ) = prayersUseCases.fetchLocation()?.let {
        _locationState.value = _locationState.value.copy(
            location = it, locale = prayersUseCases.getLocale(it)
        )

        prayersUseCases.setLocation(it)
        onDone(it)
    }
}