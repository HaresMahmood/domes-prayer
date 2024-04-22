package com.example.domes.domain.model.prayer

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime


@Entity
@Parcelize
data class PrayerTime(
    @PrimaryKey
    val prayer: @RawValue Prayer,
    val time: LocalDateTime,
    val timeWindow: LocalDateTime, // End time...
    val dayFraction: Double? = 0.0, // Fraction of the day when the prayer time occurs, calculated from an hour before Fayr to an hour after Isha
): Parcelable