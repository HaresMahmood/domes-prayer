package com.example.domes.domain.usecase

import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.example.domes.data.repository.OldPrayerTimesRespitory
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.hours

class GetPrayerTimes(
    private val repository: OldPrayerTimesRespitory
) {
    companion object {
        private fun mapPrayer(prayer: Prayer): Prayer {
            return when (prayer) {
                Prayer.FAJR -> Prayer.FAJR
                Prayer.SUNRISE -> Prayer.SUNRISE
                Prayer.DHUHR -> Prayer.DHUHR
                Prayer.ASR -> Prayer.ASR
                Prayer.MAGHRIB -> Prayer.MAGHRIB
                Prayer.ISHA -> Prayer.ISHA
                else -> Prayer.TEST
            }
        }
    }
    
    private val systemTimeZone = TimeZone.currentSystemDefault()

    operator fun invoke(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters): Pair<List<PrayerTime>, PrayerTimes> {
        val (prayerTimes, sunnahTimes) = repository.getAll(date, coordinates, parameters)

        val fajrTime = prayerTimes.fajr.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
        val sunriseTime = prayerTimes.sunrise.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
        val dhuhrTime = prayerTimes.dhuhr.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
        val asrTime = prayerTimes.asr.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
        val maghribTime = prayerTimes.maghrib.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
        val ishaTime = prayerTimes.isha.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
        val middleOfTheNightTime = sunnahTimes.middleOfTheNight.toLocalDateTime(systemTimeZone).toJavaLocalDateTime()
        val testTime = LocalDateTime.now().plusMinutes(1)

        // A value between 0 and 1 representing at what point in the day the prayer time occurs
        // i.e.: 0 is an hour before Fajr, 1 is an hour after Isha
        //       0.5 is halfway between Fajr and Isha
        fun getDayFraction(prayer: Instant): Double {
            val fajr = prayerTimes.fajr.minus(1.hours)
            val isha = prayerTimes.isha.plus(1.hours)

            val fajrToIsha = isha - fajr
            val fajrToPrayer = prayer - fajr

            return fajrToPrayer / fajrToIsha
        }

        return Pair(listOf(
            PrayerTime(
                prayer = Prayer.FAJR,
                time = fajrTime,
                dayFraction = getDayFraction(prayerTimes.fajr),
                timeWindow = sunriseTime,
            ),
            PrayerTime(
                prayer = Prayer.SUNRISE,
                time =  sunriseTime,
                // dayFraction = getDayFraction(prayerTimes.sunrise),
                timeWindow = dhuhrTime,
            ),
            PrayerTime(
                prayer = Prayer.DHUHR,
                time = dhuhrTime,
                dayFraction = getDayFraction(prayerTimes.dhuhr),
                timeWindow = asrTime,
            ),
            PrayerTime(
                prayer = Prayer.ASR,
                time = asrTime,
                dayFraction = getDayFraction(prayerTimes.asr),
                timeWindow = maghribTime,
            ),
            PrayerTime(
                prayer = Prayer.MAGHRIB,
                time = maghribTime,
                dayFraction = getDayFraction(prayerTimes.maghrib),
                timeWindow = ishaTime
            ),
            PrayerTime(
                prayer = Prayer.ISHA,
                time = ishaTime,
                dayFraction = getDayFraction(prayerTimes.isha),
                timeWindow = middleOfTheNightTime
            ),
            PrayerTime(
                prayer = Prayer.TEST,
                time = testTime,
                timeWindow = testTime.plusMinutes(5)
            )
        ), prayerTimes)
    }
}
