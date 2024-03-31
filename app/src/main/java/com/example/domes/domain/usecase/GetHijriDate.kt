package com.example.domes.domain.usecase

import java.time.LocalDate
import java.time.chrono.HijrahDate

class GetHijriDate {
    operator fun invoke(gregorianDate: LocalDate): HijrahDate {
        return HijrahDate.from(gregorianDate)
    }
}