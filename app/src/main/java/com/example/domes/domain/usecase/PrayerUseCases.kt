package com.example.domes.domain.usecase

data class PrayerUseCases(
    val getPrayerTimes: GetPrayerTimes,
    val getCurrentPrayer: GetCurrentPrayer,
    val getPrayerRecord: GetPrayerRecord,
    val addPrayerRecord: AddPrayerRecord,
    val getHijriDate: GetHijriDate,
    val getPrayerTimeLeft: GetPrayerTimeLeft,
    val sendNotification: SendNotification,
    val fetchLocation: FetchLocation,
    val getLocation: GetLocation,
    val getCity: GetCity,
    val setCalculationMethod: SetCalculationMethod,
    val getCalculationMethod: GetCalculationMethod,
    val setJuristic: SetJuristic,
    val getJuristic: GetJuristic,
    val getDayFraction: GetDayFraction,
)