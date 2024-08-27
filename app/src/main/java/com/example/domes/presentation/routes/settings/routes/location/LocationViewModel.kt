package com.example.domes.presentation.routes.settings.routes.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(LocationState())
    val state: State<LocationState> = _state

    fun onEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.ToggleAutoUpdate -> {
                _state.value = _state.value.copy(
                    isAutoUpdateEnabled = !_state.value.isAutoUpdateEnabled
                )
            }

            is LocationEvent.ExpandCoordinatesSetting -> {
                _state.value = _state.value.copy(
                    expandCoordinatesSetting = event.expanded
                        ?: !_state.value.expandCoordinatesSetting
                )
            }

            is LocationEvent.OnLatitudeTextChange -> {
                _state.value = _state.value.copy(
                    latitudeText = event.text
                )
            }

            is LocationEvent.OnLongitudeTextChange -> {
                _state.value = _state.value.copy(
                    longitudeText = event.text
                )
            }
        }
    }
}