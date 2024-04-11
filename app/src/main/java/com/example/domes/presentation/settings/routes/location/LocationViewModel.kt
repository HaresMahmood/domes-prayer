package com.example.domes.presentation.settings.routes.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(LocationState())
    val state: State<LocationState> = _state

    init {
        viewModelScope.launch {
            val coordinates = prayersUseCases.getLocation()

            _state.value = _state.value.copy(
                location = coordinates,
                city = prayersUseCases.getCity(coordinates)
            )
        }
    }

    fun onEvent(event: LocationEvent) {
        when(event) {
            is LocationEvent.RefreshLocation -> {
                viewModelScope.launch {
                    val coordinates = prayersUseCases.fetchLocation()

                    _state.value = _state.value.copy(
                        location = coordinates,
                        city = prayersUseCases.getCity(coordinates)
                    )
                }
            }
            is LocationEvent.ToggleAutoUpdate -> {
                _state.value = state.value.copy(
                    isAutoUpdateEnabled = !_state.value.isAutoUpdateEnabled
                )
            }
        }
    }
}