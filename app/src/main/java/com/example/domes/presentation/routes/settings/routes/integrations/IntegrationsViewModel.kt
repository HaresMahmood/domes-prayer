package com.example.domes.presentation.routes.settings.routes.integrations

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntegrationsViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    val _state = mutableStateOf(IntegrationsState())
    val state: State<IntegrationsState> = _state

    init {
    }

    fun onEvent(event: IntegrationsEvent) {
        when (event) {
            is IntegrationsEvent.RefreshLocation -> {
            }

            is IntegrationsEvent.ChangeCoordinates -> {
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