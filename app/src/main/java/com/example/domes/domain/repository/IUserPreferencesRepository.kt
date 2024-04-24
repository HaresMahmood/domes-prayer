package com.example.domes.domain.repository

import com.batoulapps.adhan2.Coordinates

interface IUserPreferencesRepository {
    suspend fun setLocation(latitude: Double, longitude: Double)
    suspend fun getLocation(): Coordinates?
    suspend fun setCalculationMethod(calculationMethod: Int)
    suspend fun getCalculationMethod(): Int?
    suspend fun setJuristic(juristic: Int)
    suspend fun getJuristic(): Int?
    suspend fun setNotifications(isNotificationsAllowed: Boolean)
    suspend fun getNotifications(): Boolean?
}