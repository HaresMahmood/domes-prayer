package com.example.domes.presentation.routes.qibla

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.presentation.routes.qibla.QiblaState.Companion.DEVIATION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class QiblaViewModel @Inject constructor(
    application: Application,
    private val prayersUseCases: PrayerUseCases
) : ViewModel() {
    private val sensorManager: SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensor: Sensor? = null

    private val _state = mutableStateOf(QiblaState())
    val state: State<QiblaState> = _state

    private var hapticJob: Job? = null

    private val sensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val rotation = Math.toDegrees(orientation[0].toDouble()).toFloat()
                val azimuth = (rotation + 360) % 360

                _state.value = _state.value.copy(
                    azimuth = azimuth,
                    isFacingQibla = azimuth in _state.value.qiblaDirection - DEVIATION.._state.value.qiblaDirection + DEVIATION,
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            val coordinates = prayersUseCases.getLocation()

            coordinates?.let {
                _state.value = _state.value.copy(
                    location = coordinates,
                    locale = prayersUseCases.getLocale(coordinates),
                )
            }

            _state.value.location?.let { location ->
                _state.value = _state.value.copy(
                    qiblaDirection = prayersUseCases.getQiblaDirection(location)
                )
            }
        }

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    private fun startHapticFeedbackLoop() {
        hapticJob = viewModelScope.launch {
            while (true) {
                val difference = abs(_state.value.qiblaDirection - _state.value.azimuth)

                // Trigger the haptic feedback
                _state.value.triggerHapticFeedback.postValue(Unit)

                // Calculate the delay based on the difference
                val delayMillis = calculateDelay(difference)
                delay(delayMillis)
            }
        }
    }

    override fun onCleared() {
        sensorManager.unregisterListener(sensorListener)
        hapticJob?.cancel()
    }

    private fun calculateDelay(difference: Float): Long {
        val maxDelay = 1000L
        val minDelay = 100L
        val normalizedDifference = (difference / 180).coerceIn(0f, 1f)

        return maxDelay - ((maxDelay - minDelay) * normalizedDifference).toLong()
    }
}