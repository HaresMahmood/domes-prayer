package com.example.domes.domain.usecase

import android.util.Log
import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.SunnahTimes
import com.batoulapps.adhan2.data.DateComponents
import com.example.domes.domain.model.PrayerTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.ZoneId

class GetPrayerTimes {
    companion object {
        private val systemTimeZone = TimeZone.currentSystemDefault()
    }

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
            val sunnahTimes = SunnahTimes(prayerTimes)

            val currentPrayer = prayerTimes.currentPrayer(currentTime)
            val nextPrayer = prayerTimes.nextPrayer(currentTime)

            val fajrTime = prayerTimes.fajr.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
            val sunriseTime = prayerTimes.sunrise.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
            val dhuhrTime = prayerTimes.dhuhr.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
            val asrTime = prayerTimes.asr.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
            val maghribTime = prayerTimes.maghrib.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
            val ishaTime = prayerTimes.isha.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
            val middleOfTheNightTime = sunnahTimes.middleOfTheNight.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()

            Log.d("GET-PRAYER-TIMES", sunnahTimes.toString())
            Log.d("GET-PRAYER-TIMES", prayerTimes.toString())
            Log.d("GET-PRAYER-TIMES", currentPrayer.toString())
            Log.d("GET-PRAYER-TIMES", ishaTime.toString())
                Log.d("GET-PRAYER-TIMES", middleOfTheNightTime.toString())

                listOf(
                PrayerTime(
                    prayer = com.example.domes.domain.model.Prayer.FAJR,
                    time = fajrTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.fajr),
                    timeWindow = sunriseTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.FAJR,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.FAJR,
                    hasPassed = currentTime.epochSeconds > prayerTimes.fajr.epochSeconds
                ),
                PrayerTime(
                    prayer = com.example.domes.domain.model.Prayer.SUNRISE,
                    time =  sunriseTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.sunrise),
                    timeWindow = dhuhrTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.SUNRISE,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.SUNRISE,
                    hasPassed = currentTime.epochSeconds > prayerTimes.sunrise.epochSeconds
                ),
                PrayerTime(
                    prayer = com.example.domes.domain.model.Prayer.DHUHR,
                    time = dhuhrTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.dhuhr),
                    timeWindow = asrTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.DHUHR,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.DHUHR,
                    hasPassed = currentTime.epochSeconds > prayerTimes.dhuhr.epochSeconds
                ),
                PrayerTime(
                    prayer = com.example.domes.domain.model.Prayer.ASR,
                    time = asrTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.asr),
                    timeWindow = maghribTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.ASR,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.ASR,
                    hasPassed = currentTime.epochSeconds > prayerTimes.asr.epochSeconds
                ),
                PrayerTime(
                    prayer = com.example.domes.domain.model.Prayer.MAGHRIB,
                    time = maghribTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.maghrib),
                    timeWindow = ishaTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.MAGHRIB,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.MAGHRIB,
                    hasPassed = currentTime.epochSeconds > prayerTimes.maghrib.epochSeconds
                ),
                PrayerTime(
                    prayer = com.example.domes.domain.model.Prayer.ISHA,
                    time = ishaTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.isha),
                    timeWindow = middleOfTheNightTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.ISHA,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.ISHA,
                    hasPassed = currentTime.epochSeconds > prayerTimes.isha.epochSeconds
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
