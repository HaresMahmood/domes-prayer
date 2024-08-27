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
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Madhab
import com.example.domes.data.service.NotificationService.Companion.ACTION_NOTIFICATION_ACTION_CLICKED
import com.example.domes.data.worker.PrayerTimesUpdateWorker
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.presentation.routes.settings.routes.calculation.CalculationEvent
import com.example.domes.presentation.routes.settings.routes.calculation.CalculationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
    private val _calculationState = mutableStateOf(CalculationState())

    val prayersState: State<PrayersState> = _state
    val calculationState: State<CalculationState> = _calculationState

    val workManager = WorkManager.getInstance(context)

    private var getPrayerRecordJob: Job? = null
    private var snoozePrayerJob: Job? = null
    private val currentTime = LocalDateTime.now()

    // Events

    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK && _state.value.currentPrayer != null) {
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
            getCalculationParameters().also {
                getLocation { location ->
                    viewModelScope.launch {
                        updateCurrentPrayerTimes(
                            location,
                            calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                        )
                    }
                }
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
                    _state.value.location!!,
                    _calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                )
            }

            is PrayersEvent.RefreshLocation -> {
                viewModelScope.launch {
                    fetchLocation { location ->
                        updateCurrentPrayerTimes(
                            location,
                            calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                        )
                    }
                }
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

            is PrayersEvent.SnoozePrayer -> {
                _state.value =
                    _state.value.copy(snoozedUntil = LocalDateTime.now().plus(event.duration))

                snoozePrayerJob?.cancel()
                snoozePrayerJob = viewModelScope.launch {
                    delay(event.duration.toMillis())
                    _state.value = _state.value.copy(snoozedUntil = null)
                }
            }

            is PrayersEvent.SetOverarchingDropdownVisibility -> {
                _state.value = _state.value.copy(
                    showOverarchingDropdown = event.isVisible
                        ?: !_state.value.showOverarchingDropdown
                )
            }

            is PrayersEvent.SetSnoozeDialogVisibility -> {
                _state.value = _state.value.copy(
                    showSnoozeDialog = event.isVisible
                        ?: !_state.value.showSnoozeDialog
                )
            }

            is PrayersEvent.ChangeCoordinates -> {
                val coordinates = Coordinates(event.latitude, event.longitude)

                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        location = coordinates,
                        locale = prayersUseCases.getLocale(coordinates)
                    )

                    if (_state.value.selectedDate == LocalDate.now()) {
                        updateCurrentPrayerTimes(
                            coordinates,
                            calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                        )
                    } else {
                        getPrayerTimes(
                            _state.value.selectedDate,
                            coordinates,
                            calculationState.value.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                        )
                    }

                    prayersUseCases.setLocation(coordinates)
                }
            }
        }
    }

    fun onEvent(event: CalculationEvent) {
        when (event) {
            is CalculationEvent.ChangeCalculationMethod -> {
                _calculationState.value = _calculationState.value.copy(
                    calculationMethod = event.calculationMethod
                )

                viewModelScope.launch {
                    prayersUseCases.setCalculationMethod(event.calculationMethod)
                }

                updateCurrentPrayerTimes(
                    _state.value.location!!,
                    event.calculationMethod.parameters.copy(madhab = _calculationState.value.juristic)
                )
            }

            is CalculationEvent.ChangeJuristic -> {
                _calculationState.value = _calculationState.value.copy(
                    juristic = event.juristic
                )

                viewModelScope.launch {
                    prayersUseCases.setJuristic(event.juristic)
                }

                updateCurrentPrayerTimes(
                    _state.value.location!!,
                    _calculationState.value.calculationMethod.parameters.copy(madhab = event.juristic)
                )
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
        coordinates: Coordinates,
        parameters: CalculationParameters,
    ) {
        _state.value = _state.value.copy(isLoading = true)

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<PrayerTimesUpdateWorker>()
            .build()
        workManager.enqueueUniqueWork(
            "oneTimeWorkRequest${System.currentTimeMillis()}",
            ExistingWorkPolicy.KEEP,
            oneTimeWorkRequest
        )

        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observeForever { info ->
            if (info.state == WorkInfo.State.SUCCEEDED) {
                viewModelScope.launch {
                    val currentTime = LocalDateTime.now()
                    val currentDate = currentTime.toLocalDate()
                    val (_, rawPrayerTimes) = prayersUseCases.getPrayerTimes(
                        currentDate,
                        coordinates,
                        parameters
                    )

                    println("re-eval triggered with parameters: $currentDate, ${coordinates.latitude}, ${coordinates.longitude}, $parameters")
                    println("raw prayer times: ${rawPrayerTimes.fajr}")

                    val prayerTimes = prayersUseCases.getCurrentPrayerTimes().first()
                    val currentPrayer = prayersUseCases.getCurrentPrayer(prayerTimes, currentTime)

                    getPrayerRecord(currentDate)

                    _state.value = _state.value.copy(
                        isLoading = false,
                        currentPrayerTimes = prayerTimes,
                        currentRawPrayerTimes = rawPrayerTimes,
                        currentPrayer = currentPrayer,
                        selectedHijriDate = prayersUseCases.getHijriDate(_state.value.selectedDate),
                        dayFraction = prayersUseCases.getDayFraction(rawPrayerTimes, currentTime)
                    )

                    currentPrayer?.let {
                        _state.value = _state.value.copy(
                            timeLeft = prayersUseCases.getPrayerTimeLeft(currentTime, it),
                            totalTime = prayersUseCases.getPrayerTimeLeft(
                                it.time,
                                it
                            ),
                        )
                    }
                }
            }
        }
    }


    private fun getPrayerTimes(
        date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters,
    ) {
        getPrayerRecord(date)

        _state.value = _state.value.copy(
            selectedPrayerTimes = prayersUseCases.getPrayerTimes(
                date, coordinates, parameters
            ).first
        )
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
        _state.value = _state.value.copy(
            location = it, locale = prayersUseCases.getLocale(it)
        )

        onDone(it)
    }


    private suspend fun getCalculationParameters() {
        _calculationState.value = _calculationState.value.copy(
            calculationMethod = prayersUseCases.getCalculationMethod()
                ?: CalculationMethod.MUSLIM_WORLD_LEAGUE,
            juristic = prayersUseCases.getJuristic() ?: Madhab.SHAFI,
        )
    }

    private suspend fun fetchLocation(
        onDone: (Coordinates) -> Unit,
    ) = prayersUseCases.fetchLocation()?.let {
        _state.value = _state.value.copy(
            location = it, locale = prayersUseCases.getLocale(it)
        )

        prayersUseCases.setLocation(it)
        onDone(it)
    }
}