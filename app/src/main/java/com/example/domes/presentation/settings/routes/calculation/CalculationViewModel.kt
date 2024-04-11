package com.example.domes.presentation.settings.routes.calculation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            )
        }
    }

    fun onEvent(event: CalculationEvent) {
        when(event) {
            is CalculationEvent.ChangeCalculationParameter -> {
                viewModelScope.launch {

                    _state.value = _state.value.copy(

                    )
                }
            }
        }
    }
}