package com.example.domes.presentation.routes.settings.routes.appearance

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    val _state = mutableStateOf(AppearanceState())
    val state: State<AppearanceState> = _state

    init {

    }

    fun onEvent(event: AppearanceEvent) {
        when (event) {
            is AppearanceEvent.RefreshLocation -> {
            }

            is AppearanceEvent.ChangeCoordinates -> {

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