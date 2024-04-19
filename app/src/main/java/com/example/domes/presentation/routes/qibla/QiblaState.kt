package com.example.domes.presentation.routes.qibla

import androidx.lifecycle.MutableLiveData
import com.batoulapps.adhan2.Coordinates

data class QiblaState(
    val azimuth: Float = 0f,
    val qiblaDirection: Float = 0f,
    val isFacingQibla: Boolean = false,
    val location: Coordinates? = null,
    val locale: String = "",
    val triggerHapticFeedback: MutableLiveData<Unit> = MutableLiveData<Unit>()
) {
    companion object {
        val DEVIATION = 10f
    }
}