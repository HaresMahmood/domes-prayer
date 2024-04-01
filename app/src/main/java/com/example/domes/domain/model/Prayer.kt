package com.example.domes.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDateTime

//sealed class Icon {
//    data class Vector(val imageVector: ImageVector) : Icon()
//    data class Paint(val painter: Painter) : Icon()
//}

data class Prayer(
    val name: String,
    val arabicName: String,
    val icon: ImageVector,
    val displayable: Boolean = true,
) {
    companion object {
        val FAJR = Prayer("Fajr", "فجر", Icons.Outlined.WbSunny)
        val SUNRISE = Prayer("Sunrise", "شروق", Icons.Outlined.WbSunny, false)
        val DHUHR = Prayer("Dhuhr", "ظهر", Icons.Outlined.WbSunny)
        val ASR = Prayer("Asr", "عصر", Icons.Outlined.WbSunny)
        val MAGHRIB = Prayer("Maghrib", "مغرب", Icons.Outlined.WbSunny)
        val ISHA = Prayer("Isha", "عشاء", Icons.Outlined.WbSunny)
    }
}