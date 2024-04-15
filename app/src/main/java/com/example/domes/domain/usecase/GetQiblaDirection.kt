package com.example.domes.domain.usecase

import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Qibla

class GetQiblaDirection {
    operator fun invoke(coordinates: Coordinates): Float {
        return Qibla(coordinates).direction.toFloat()
    }
}