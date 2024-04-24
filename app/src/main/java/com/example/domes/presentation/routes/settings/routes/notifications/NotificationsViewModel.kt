package com.example.domes.presentation.routes.settings.routes.notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(NotificationsState())
    val state: State<NotificationsState> = _state

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isNotificationsAllowed = prayersUseCases.getNotificationsAllowed(),
            )
        }
    }

    fun onEvent(event: NotificationsEvent) {
        when(event) {
            is NotificationsEvent.ChangeNotificationsAllowed -> {
                val isAllowed = event.isNotificationsAllowed ?: !_state.value.isNotificationsAllowed

                _state.value = _state.value.copy(
                    isNotificationsAllowed = isAllowed
                )

                viewModelScope.launch {
                    prayersUseCases.setNotificationsAllowed(isAllowed)
                }
            }
        }
    }
}