package com.example.domes.domain.usecase

import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.data.DateComponents
import kotlinx.datetime.Instant
import java.time.LocalDate
import java.time.ZoneId

class GetPrayerTimes {
    operator fun invoke(date: LocalDate, coordinates: Coordinates, parameters: CalculationParameters): PrayerTimes {
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
}   