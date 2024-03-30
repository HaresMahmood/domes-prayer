package com.example.domes.presentation.prayers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayersViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases
) : ViewModel() {
    private val _state = mutableStateOf(PrayersState())
    val state: State<PrayersState> = _state

    fun onEvent(event: PrayersEvent) {
        when(event) {
            is PrayersEvent.CurrentPrayer -> {
                _state.value = _state.value.copy(

                )
            }
            is PrayersEvent.ChangeDate -> {
                _state.value = _state.value.copy(
                    selectedDate = event.date
                )

                viewModelScope.launch {
                    _state.value.location?.let {
                        prayersUseCases.getPrayerTimes(_state.value.selectedDate,
                            it, _state.value.calculationParameters)
                    }
                }
            }
            is PrayersEvent.ChangeLocation -> {
                _state.value = _state.value.copy(
                    location = event.coordinates
                )

                viewModelScope.launch {
                    _state.value.location?.let {
                        prayersUseCases.getPrayerTimes(_state.value.selectedDate,
                            it, _state.value.calculationParameters)
                    }
                }
            }
        }
    }


}