package com.example.domes.data.repository

import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Prayer
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.SunnahTimes
import com.batoulapps.adhan2.data.DateComponents
import kotlinx.datetime.Instant
import java.time.LocalDate
import java.time.ZoneId

class OldPrayerTimesRespitory {
   fun getAll(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters): Pair<PrayerTimes, SunnahTimes> {
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
           return Pair(prayerTimes, sunnahTimes)
       }
   }

   fun getTimes(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters): PrayerTimes {
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
       )
   }

   fun getSunnah(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters): SunnahTimes {
       return SunnahTimes(
           PrayerTimes(
               coordinates,
               DateComponents.from(
                   Instant.fromEpochMilliseconds(
                       date.atStartOfDay(
                           ZoneId.systemDefault()
                       ).toInstant().toEpochMilli()
                   )
               ),
               parameters
           )
       )
   }

   fun getSunnah(prayerTimes: PrayerTimes): SunnahTimes {
       return SunnahTimes(prayerTimes)
   }

   fun getCurrentPrayer(prayerTimes: PrayerTimes, currentTime: Instant): Prayer {
       return prayerTimes.currentPrayer(currentTime)
   }

    fun getNextPrayer(prayerTimes: PrayerTimes, currentTime: Instant): Prayer {
         return prayerTimes.nextPrayer(currentTime)
    }
}