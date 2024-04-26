package com.example.domes.presentation.routes.settings.routes.notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domes.domain.model.prayer.PrayerNotification
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.presentation.common.utility.addOrReplace
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
                var notification = _state.value.prayerNotifications.find { it.prayer == event.prayer }?.copy()

                if (notification != null) {
                    var isAllowed = event.isNotificationsAllowed ?: !notification.isNotificationAllowed

                    notification.isNotificationAllowed = isAllowed
                }
                else {
                    notification = PrayerNotification(event.prayer, isNotificationAllowed = false)
                }

                val updatedNotifications = _state.value.copy().prayerNotifications.toMutableList()
                updatedNotifications.addOrReplace(
                    { it.prayer == event.prayer },
                    notification
                )

                _state.value = _state.value.copy(
                    prayerNotifications = updatedNotifications
                )

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