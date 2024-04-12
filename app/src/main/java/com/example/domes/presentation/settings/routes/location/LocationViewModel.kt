package com.example.domes.presentation.settings.routes.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan2.Coordinates
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
                locale = prayersUseCases.getLocale(coordinates)
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
                        locale = prayersUseCases.getLocale(coordinates)
                    )
                }
            }
            is LocationEvent.ChangeCoordinates -> {
                val coordinates = Coordinates(event.latitude, event.longitude)

                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        location = coordinates,
                        locale = prayersUseCases.getLocale(coordinates)
                    )

                    prayersUseCases.setLocation(coordinates)
                }
            }
            is LocationEvent.ToggleAutoUpdate -> {
                _state.value = state.value.copy(
                    isAutoUpdateEnabled = !_state.value.isAutoUpdateEnabled
                )
            }
            is LocationEvent.SetCoordinatesDialogVisibility -> {
                _state.value = _state.value.copy(
                    showCoordinatesDialog = event.isVisible ?: !_state.value.showCoordinatesDialog
                )
            }
        }
    }
}