package com.example.domes.domain.usecase

data class PrayerUseCases(
    val getPrayerTimes: GetPrayerTimes,
    val getPrayerRecord: GetPrayerRecord,
    val getHijriDate: GetHijriDate
)