package com.example.domes.domain.usecase

data class PrayerUseCases(
    val getPrayerTimes: GetPrayerTimes,
    val getCurrentPrayer: GetCurrentPrayer,
    val getPrayerRecord: GetPrayerRecord,
    val getHijriDate: GetHijriDate,
    val getPrayerTimeLeft: GetPrayerTimeLeft,
    val sendNotification: SendNotification,
    val getCity: GetCity
)