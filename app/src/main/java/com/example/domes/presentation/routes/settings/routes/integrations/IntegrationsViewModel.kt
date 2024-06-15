package com.example.domes.presentation.routes.settings.routes.integrations

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
class IntegrationsViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    val _state = mutableStateOf(IntegrationsState())
    val state: State<IntegrationsState> = _state

    init {
        viewModelScope.launch {
            val coordinates = prayersUseCases.getLocation()

            _state.value = _state.value.copy(
                location = coordinates,
                locale = prayersUseCases.getLocale(coordinates)
            )
        }
    }

    fun onEvent(event: IntegrationsEvent) {
        when (event) {
            is IntegrationsEvent.RefreshLocation -> {
                viewModelScope.launch {
                    val coordinates = prayersUseCases.fetchLocation()

                    _state.value = _state.value.copy(
                        location = coordinates,
                        locale = prayersUseCases.getLocale(coordinates)
                    )
                }
            }

            is IntegrationsEvent.ChangeCoordinates -> {
                val coordinates = Coordinates(event.latitude, event.longitude)

                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        location = coordinates,
                        locale = prayersUseCases.getLocale(coordinates)
                    )

                    prayersUseCases.setLocation(coordinates)
                }
            }

            is IntegrationsEvent.ToggleAutoUpdate -> {
                _state.value = _state.value.copy(
                    isAutoUpdateEnabled = !_state.value.isAutoUpdateEnabled
                )
            }

            is IntegrationsEvent.SetCoordinatesDialogVisibility -> {
                _state.value = _state.value.copy(
                    showCoordinatesDialog = event.isVisible ?: !_state.value.showCoordinatesDialog
                )
            }
        }
    }
}