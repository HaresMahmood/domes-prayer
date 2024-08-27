package com.example.domes.presentation.routes.settings.routes.location

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
class LocationViewModel @Inject constructor(
    private val prayerUseCases: PrayerUseCases,
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
                val error = !validateInput(event.text)

                _state.value = _state.value.copy(
                    showLatitudeTextError = error, latitudeText = event.text
                )

                if (!error && !_state.value.showLongitudeTextError) {
                    onEvent(
                        LocationEvent.FetchEditedLocale(
                            Coordinates(
                                event.text.toDouble(),
                                _state.value.longitudeText.toDouble()
                            )
                        )
                    )
                }
            }

            is LocationEvent.OnLongitudeTextChange -> {
                val error = !validateInput(event.text)

                _state.value = _state.value.copy(
                    showLongitudeTextError = error, longitudeText = event.text
                )

                if (!error && !_state.value.showLatitudeTextError) {
                    onEvent(
                        LocationEvent.FetchEditedLocale(
                            Coordinates(
                                _state.value.latitudeText.toDouble(),
                                event.text.toDouble()
                            )
                        )
                    )
                }
            }

            is LocationEvent.FetchEditedLocale -> {
                println("Fetching edited locale for ${event.coordinates}")
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        editedLocale = prayerUseCases.getLocale(event.coordinates)
                    )
                }
            }

            is LocationEvent.PrefillCoordinatesText -> {
                _state.value = _state.value.copy(
                    latitudeText = event.coordinates.latitude.toString(),
                    longitudeText = event.coordinates.longitude.toString()
                )
            }

            LocationEvent.ResetCoordinatesText -> {
                _state.value = _state.value.copy(
                    latitudeText = "", longitudeText = "", editedLocale = null
                )
            }
        }
    }

    private fun validateInput(input: String): Boolean {
        val pattern = Regex("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)")

        return pattern.matches(input) && input.toDoubleOrNull()?.let { it in -90.0..90.0 } == true
    }
}