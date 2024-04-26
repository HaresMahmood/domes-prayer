package com.example.domes.presentation.routes.settings.routes.notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domes.domain.model.prayer.PrayerNotification
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        getNotification()
    }

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            is NotificationsEvent.ChangeNotificationsAllowed -> {
                val isAllowed = event.isNotificationsAllowed ?: !_state.value.isNotificationsAllowed

                _state.value = _state.value.copy(
                    isNotificationsAllowed = isAllowed
                )

                viewModelScope.launch {
                    prayersUseCases.setNotificationsAllowed(isAllowed)
                }
            }
            is NotificationsEvent.ChangePrayerNotification -> {
                val existingNotification = _state.value.prayerNotifications.find { it.prayer == event.prayer }
                val notification = existingNotification
                    ?.copy(isNotificationAllowed = event.isNotificationsAllowed ?: !existingNotification.isNotificationAllowed)
                    ?: PrayerNotification(event.prayer, isNotificationAllowed = event.isNotificationsAllowed ?: false)

                _state.value = _state.value.copy(prayerNotifications = _state.value.prayerNotifications.toMutableList().apply {
                    val index = indexOfFirst { it.prayer == event.prayer }
                    if (index != -1) this[index] = notification else add(notification)
                })

                viewModelScope.launch {
                    prayersUseCases.addPrayerNotification(notification)
                }
            }
        }
    }

    private fun getNotification() {
        prayersUseCases.getPrayerNotifications().onEach { notifications ->
            _state.value = _state.value.copy(
                prayerNotifications = notifications
            )
        }.launchIn(viewModelScope)
    }
}