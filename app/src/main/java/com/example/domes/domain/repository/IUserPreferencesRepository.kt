package com.example.domes.domain.repository

import com.batoulapps.adhan2.Coordinates

interface IUserPreferencesRepository {
    suspend fun saveLocation(latitude: Double, longitude: Double)
    suspend fun getLocation(): Coordinates?
}