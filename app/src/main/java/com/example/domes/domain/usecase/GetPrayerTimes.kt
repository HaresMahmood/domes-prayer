package com.example.domes.domain.usecase

import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.data.DateComponents
import com.example.domes.domain.model.PrayerTime
import com.example.domes.domain.model.Prayer
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class GetPrayerTimes {
    operator fun invoke(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters): List<PrayerTime> {
        val currentTime = Instant.fromEpochMilliseconds(System.currentTimeMillis())

        return PrayerTimes(
            coordinates,
            DateComponents.from(
                Instant.fromEpochMilliseconds(
                    date.atStartOfDay(
                        ZoneId.systemDefault()
                    ).toInstant().toEpochMilli()
                )
            ),
            parameters
        ).let { prayerTimes ->
            listOf(
                PrayerTime(
                    prayer = Prayer.FAJR,
                    time = prayerTimes.fajr.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime(),
                    dayFraction = getDayFraction(currentTime, prayerTimes.fajr),
                    isCurrent = currentTime.epochSeconds in prayerTimes.fajr.epochSeconds..prayerTimes.sunrise.epochSeconds,
                    isNext = currentTime.epochSeconds in prayerTimes.sunrise.epochSeconds..prayerTimes.dhuhr.epochSeconds,
                    hasPassed = currentTime.epochSeconds > prayerTimes.fajr.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.SUNRISE,
                    time = prayerTimes.sunrise.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime(),
                    dayFraction = getDayFraction(currentTime, prayerTimes.sunrise),
                    isCurrent = currentTime.epochSeconds in prayerTimes.sunrise.epochSeconds..prayerTimes.dhuhr.epochSeconds,
                    isNext = currentTime.epochSeconds in prayerTimes.dhuhr.epochSeconds..prayerTimes.asr.epochSeconds,
                    hasPassed = currentTime.epochSeconds > prayerTimes.sunrise.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.DHUHR,
                    time = prayerTimes.dhuhr.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime(),
                    dayFraction = getDayFraction(currentTime, prayerTimes.dhuhr),
                    isCurrent = currentTime.epochSeconds in prayerTimes.dhuhr.epochSeconds..prayerTimes.asr.epochSeconds,
                    isNext = currentTime.epochSeconds in prayerTimes.asr.epochSeconds..prayerTimes.maghrib.epochSeconds,
                    hasPassed = currentTime.epochSeconds > prayerTimes.dhuhr.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.ASR,
                    time = prayerTimes.asr.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime(),
                    dayFraction = getDayFraction(currentTime, prayerTimes.asr),
                    isCurrent = currentTime.epochSeconds in prayerTimes.asr.epochSeconds..prayerTimes.maghrib.epochSeconds,
                    isNext = currentTime.epochSeconds in prayerTimes.maghrib.epochSeconds..prayerTimes.isha.epochSeconds,
                    hasPassed = currentTime.epochSeconds > prayerTimes.asr.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.MAGHRIB,
                    time = prayerTimes.maghrib.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime(),
                    dayFraction = getDayFraction(currentTime, prayerTimes.maghrib),
                    isCurrent = currentTime.epochSeconds in prayerTimes.maghrib.epochSeconds..prayerTimes.isha.epochSeconds,
                    isNext = currentTime.epochSeconds in prayerTimes.isha.epochSeconds..prayerTimes.isha.epochSeconds + 60 * 60,
                    hasPassed = currentTime.epochSeconds > prayerTimes.maghrib.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.ISHA,
                    time = prayerTimes.isha.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime(),
                    dayFraction = getDayFraction(currentTime, prayerTimes.isha),
                    isCurrent = currentTime.epochSeconds in prayerTimes.isha.epochSeconds..prayerTimes.isha.epochSeconds + 60 * 60,
                    isNext = false,
                    hasPassed = currentTime.epochSeconds > prayerTimes.isha.epochSeconds && currentTime.epochSeconds < prayerTimes.isha.epochSeconds + 60 * 60
                )
            )
        }
    }

    private fun getDayFraction(currentTime: Instant, prayerTime: Instant): Double {
        val dayLength = 24 * 60 * 60
        val timeSinceFajr = currentTime.epochSeconds - prayerTime.epochSeconds

        return timeSinceFajr.toDouble() / dayLength
    }
}
