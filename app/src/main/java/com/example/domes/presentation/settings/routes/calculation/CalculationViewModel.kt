package com.example.domes.presentation.settings.routes.calculation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Madhab
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalculationViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(CalculationState())
    val state: State<CalculationState> = _state

    init {
        viewModelScope.launch {
            val coordinates = prayersUseCases.getLocation()
            
            _state.value = _state.value.copy(
                calculationMethod = prayersUseCases.getCalculationMethod() ?: CalculationMethod.MUSLIM_WORLD_LEAGUE,
                juristic = prayersUseCases.getJuristic() ?: Madhab.SHAFI,
            )

            coordinates?.let {
                _state.value = _state.value.copy(
                    prayerTimes = prayersUseCases.getPrayerTimes(
                        LocalDate.now(),
                        it,
                        _state.value.calculationMethod.parameters.copy(madhab = _state.value.juristic)
                    ).first
                )
            }
        }
    }

    fun onEvent(event: CalculationEvent) {
        when(event) {
            is CalculationEvent.ChangeCalculationMethod -> {
                _state.value = _state.value.copy(
                    calculationMethod = event.calculationMethod
                )

                viewModelScope.launch {
                    prayersUseCases.setCalculationMethod(event.calculationMethod)
                }
            }
            is CalculationEvent.ChangeJuristic -> {
                _state.value = _state.value.copy(
                    juristic = event.juristic
                )

                viewModelScope.launch {
                    prayersUseCases.setJuristic(event.juristic)
                }
            }
            is CalculationEvent.ResetSettings -> {
                viewModelScope.launch {
                    val coordinates = prayersUseCases.getLocation()

                    _state.value = _state.value.copy(
                        calculationMethod = CalculationMethod.MUSLIM_WORLD_LEAGUE,
                        juristic = Madhab.SHAFI,
                    )

                    prayersUseCases.setCalculationMethod(_state.value.calculationMethod)
                    prayersUseCases.setJuristic(_state.value.juristic)

                    coordinates?.let {
                        _state.value = _state.value.copy(
                            prayerTimes = prayersUseCases.getPrayerTimes(
                                LocalDate.now(),
                                it,
                                _state.value.calculationMethod.parameters.copy(madhab = _state.value.juristic)
                            ).first
                        )
                    }
                }
            }
            is CalculationEvent.SetCalculationMethodDialogVisibility -> {
                _state.value = _state.value.copy(
                    showCalculationMethodDialog = event.isVisible ?: !_state.value.showCalculationMethodDialog
                )
            }
            is CalculationEvent.SetJuristicDialogVisibility -> {
                _state.value = _state.value.copy(
                    showJuristicDialog = event.isVisible ?: !_state.value.showJuristicDialog
                )
            }
        }
    }
}