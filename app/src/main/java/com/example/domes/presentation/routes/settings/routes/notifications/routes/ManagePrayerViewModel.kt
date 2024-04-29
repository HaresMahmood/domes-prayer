package com.example.domes.presentation.routes.settings.routes.notifications.routes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domes.domain.model.prayer.Notification
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerNotification
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagePrayerViewModel @Inject constructor(
    private val prayersUseCases: PrayerUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ManagePrayerState())
    val state: State<ManagePrayerState> = _state

    // TODO: Should these be properties or something else?
    private val prayer = Prayer.fromString(savedStateHandle.get<String>("prayer"))

    init {
        viewModelScope.launch {
            (prayer.let {
                prayersUseCases.getPrayerNotification(it)
            } ?: PrayerNotification(prayer)).also {
                _state.value = _state.value.copy(
                    notification = it
                )
            }
        }
    }

    fun onEvent(event: ManagePrayerEvent) {
        when (event) {
            is ManagePrayerEvent.ChangeNotificationsAllowed -> {
                val isAllowed = event.isNotificationAllowed ?: (_state.value.notification?.isNotificationAllowed == false)
                val updatedNotification = _state.value.notification?.copy(isNotificationAllowed = isAllowed)

                _state.value = _state.value.copy(
                    notification = updatedNotification
                )

                viewModelScope.launch {
                    updatedNotification?.let { prayersUseCases.addPrayerNotification(it) }
                }
            }
            is ManagePrayerEvent.ChangeExpandedNotification -> {
                println("Notification: ${event.notificationIndex}")

                _state.value = _state.value.copy(
                    expandedNotificationIndex = event.notificationIndex
                )
            }
            is ManagePrayerEvent.AddNotification -> {
                val updatedNotifications = _state.value.notification?.notifications?.toMutableList()?.apply {
                    add(Notification())
                } ?: mutableListOf(Notification())

                val updatedNotification = _state.value.notification?.copy(
                    notifications = updatedNotifications
                )

                _state.value = _state.value.copy(
                    notification = updatedNotification
                )

                viewModelScope.launch {
                    updatedNotification?.let { prayersUseCases.addPrayerNotification(it) }
                }
            }
            is ManagePrayerEvent.RemoveNotification -> {
                val updatedNotifications = _state.value.notification?.notifications?.toMutableList()?.apply {
                    remove(event.notification)
                } ?: mutableListOf(Notification())

                val updatedNotification = _state.value.notification?.copy(
                    notifications = updatedNotifications
                )

                _state.value = _state.value.copy(
                    notification = updatedNotification
                )

                viewModelScope.launch {
                    updatedNotification?.let { prayersUseCases.addPrayerNotification(it) }
                }
            }
        }
    }
}