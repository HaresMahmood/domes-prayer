package com.example.domes.presentation.routes.settings.routes.appearance

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
class AppearanceViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    val _state = mutableStateOf(AppearanceState())
    val state: State<AppearanceState> = _state

    init {
        viewModelScope.launch {
            val coordinates = prayersUseCases.getLocation()

            _state.value = _state.value.copy(
                location = coordinates,
                locale = prayersUseCases.getLocale(coordinates)
            )
        }
    }

    fun onEvent(event: AppearanceEvent) {
        when(event) {
            is AppearanceEvent.RefreshLocation -> {
                viewModelScope.launch {
                    val coordinates = prayersUseCases.fetchLocation()

                    _state.value = _state.value.copy(
                        location = coordinates,
                        locale = prayersUseCases.getLocale(coordinates)
                    )
                }
            }
            is AppearanceEvent.ChangeCoordinates -> {
                val coordinates = Coordinates(event.latitude, event.longitude)

                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        location = coordinates,
                        locale = prayersUseCases.getLocale(coordinates)
                    )

                    prayersUseCases.setLocation(coordinates)
                }
            }
            is AppearanceEvent.ToggleAutoUpdate -> {
                _state.value = _state.value.copy(
                    isAutoUpdateEnabled = !_state.value.isAutoUpdateEnabled
                )
            }
            is AppearanceEvent.SetCoordinatesDialogVisibility -> {
                _state.value = _state.value.copy(
                    showCoordinatesDialog = event.isVisible ?: !_state.value.showCoordinatesDialog
                )
            }
        }
    }
}