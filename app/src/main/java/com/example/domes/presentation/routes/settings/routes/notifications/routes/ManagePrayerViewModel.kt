package com.example.domes.presentation.routes.settings.routes.notifications.routes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private lateinit var notification: PrayerNotification // TODO: Funky

    init {
        viewModelScope.launch {
            notification = prayer.let {
                prayersUseCases.getPrayerNotification(it)
            } ?: PrayerNotification(prayer).also {
                _state.value = _state.value.copy(
                    isNotificationAllowed = it.isNotificationAllowed,
                    notifications = it.notifications
                )
            }
        }
    }

    fun onEvent(event: ManagePrayerEvent) {
        when (event) {
            is ManagePrayerEvent.ChangeNotificationsAllowed -> {
                val isAllowed = event.isNotificationAllowed ?: !_state.value.isNotificationAllowed

                _state.value = _state.value.copy(
                    isNotificationAllowed = isAllowed
                )

                notification.isNotificationAllowed = isAllowed

                viewModelScope.launch {
                    prayersUseCases.addPrayerNotification(notification)
                }
            }
        }
    }
}