package com.example.domes.presentation.settings.routes.calculation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan2.Madhab
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculationViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(CalculationState())
    val state: State<CalculationState> = _state

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                juristic = prayersUseCases.getJuristic() ?: Madhab.SHAFI,
            )
        }
    }

    fun onEvent(event: CalculationEvent) {
        when(event) {
            is CalculationEvent.ChangeJuristic -> {
                _state.value = _state.value.copy(
                    juristic = event.juristic
                )

                viewModelScope.launch {
                    prayersUseCases.setJuristic(event.juristic)
                }
            }
            is CalculationEvent.SetJuristicDialogVisibility -> {
                _state.value = _state.value.copy(
                    showJuristicDialog = event.isVisible ?: !_state.value.showJuristicDialog
                )
            }
        }
    }
}