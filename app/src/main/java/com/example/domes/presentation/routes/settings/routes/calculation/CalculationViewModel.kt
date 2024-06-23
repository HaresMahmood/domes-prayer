package com.example.domes.presentation.routes.settings.routes.calculation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Madhab
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculationViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
    private val _calculationState: MutableState<CalculationState>,
) : ViewModel() {
    val state: State<CalculationState> = _calculationState

    init {
        viewModelScope.launch {
            _calculationState.value = _calculationState.value.copy(
                calculationMethod = prayersUseCases.getCalculationMethod()
                    ?: CalculationMethod.MUSLIM_WORLD_LEAGUE,
                juristic = prayersUseCases.getJuristic() ?: Madhab.SHAFI,
            )
        }
    }
}