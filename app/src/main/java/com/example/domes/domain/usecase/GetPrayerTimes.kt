package com.example.domes.domain.usecase

import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.SunnahTimes
import com.batoulapps.adhan2.data.DateComponents
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.minutes

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

            val testTime = LocalDateTime.now().plusMinutes(1)

            listOf(
                PrayerTime(
                    prayer = Prayer.FAJR,
                    time = fajrTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.fajr),
                    timeWindow = sunriseTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.FAJR,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.FAJR,
                    hasPassed = currentTime.epochSeconds > prayerTimes.fajr.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.SUNRISE,
                    time =  sunriseTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.sunrise),
                    timeWindow = dhuhrTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.SUNRISE,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.SUNRISE,
                    hasPassed = currentTime.epochSeconds > prayerTimes.sunrise.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.DHUHR,
                    time = dhuhrTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.dhuhr),
                    timeWindow = asrTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.DHUHR,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.DHUHR,
                    hasPassed = currentTime.epochSeconds > prayerTimes.dhuhr.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.ASR,
                    time = asrTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.asr),
                    timeWindow = maghribTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.ASR,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.ASR,
                    hasPassed = currentTime.epochSeconds > prayerTimes.asr.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.MAGHRIB,
                    time = maghribTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.maghrib),
                    timeWindow = ishaTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.MAGHRIB,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.MAGHRIB,
                    hasPassed = currentTime.epochSeconds > prayerTimes.maghrib.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.ISHA,
                    time = ishaTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.isha),
                    timeWindow = middleOfTheNightTime,
                    isCurrent = currentPrayer == com.batoulapps.adhan2.Prayer.ISHA,
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.ISHA,
                    hasPassed = currentTime.epochSeconds > prayerTimes.isha.epochSeconds
                ),
                PrayerTime(
                    prayer = Prayer.TEST,
                    time = testTime,
                    dayFraction = getDayFraction(currentTime, prayerTimes.isha),
                    timeWindow = testTime.plusMinutes(5),
                    isCurrent = currentTime > currentTime.plus(1.minutes) && currentTime < currentTime.plus(5.minutes),
                    isNext = nextPrayer == com.batoulapps.adhan2.Prayer.ISHA,
                    hasPassed = currentTime.epochSeconds > currentTime.plus(1.minutes).toEpochMilliseconds()
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
