package com.example.domes.domain.usecase

import com.batoulapps.adhan2.PrayerTimes
import kotlinx.datetime.toKotlinInstant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.hours

class GetDayFraction {
    private val systemTimeZone = ZoneId.systemDefault()

    operator fun invoke(prayerTimes: PrayerTimes, currentTime: LocalDateTime): Float {
        val fajr = prayerTimes.fajr.minus(1.hours)
        val isha = prayerTimes.isha.plus(1.hours)
        val time = currentTime.atZone(systemTimeZone).toInstant().toKotlinInstant()

        val fajrToIsha = isha - fajr
        val fajrToTime = time - fajr

        println("DAYFRACTION: ${fajr},  ${isha}, ${time}")

        println("DAYFRACTION: ${fajrToTime / fajrToIsha}")

        return minOf((fajrToTime / fajrToIsha).toFloat(), 1.0f)
    }
}