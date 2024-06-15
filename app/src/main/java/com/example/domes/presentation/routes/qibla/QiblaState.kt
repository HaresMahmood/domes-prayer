package com.example.domes.presentation.routes.qibla

import androidx.lifecycle.MutableLiveData
import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.model.location.Locale

data class QiblaState(
    val azimuth: Float = 0f,
    val qiblaDirection: Float = 0f,
    val isFacingQibla: Boolean = false,
    val location: Coordinates? = null,
    val locale: Locale = Locale.unknown(),
    val triggerHapticFeedback: MutableLiveData<Unit> = MutableLiveData<Unit>()
) {
    companion object {
        val DEVIATION = 10f
    }
}