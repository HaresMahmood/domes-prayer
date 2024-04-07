package com.example.domes.domain.model.prayer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime

@Parcelize
data class PrayerTime(
    val prayer: @RawValue Prayer,
    val time: LocalDateTime,
    val dayFraction: Double? = 0.0, // Fraction of the day when the prayer time occurs, calculated from an hour before Fayr to an hour after Isha
    val timeWindow: LocalDateTime,
): Parcelable