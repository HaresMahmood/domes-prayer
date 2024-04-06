package com.example.domes.domain.model.prayer

import java.time.LocalDateTime


data class PrayerTime(
    val prayer: Prayer,
    val time: LocalDateTime,
    val dayFraction: Double? = 0.0, // Fraction of the day when the prayer time occurs, calculated from an hour before Fayr to an hour after Isha
    val timeWindow: LocalDateTime,
)