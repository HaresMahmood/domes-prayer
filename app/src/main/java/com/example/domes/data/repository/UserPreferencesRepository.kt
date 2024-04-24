package com.example.domes.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.repository.IUserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
): IUserPreferencesRepository {
    object PreferencesKeys {
        val LOCATION_LATITUDE = doublePreferencesKey("location_latitude")
        val LOCATION_LONGITUDE = doublePreferencesKey("location_longitude")

        val CALCULATION_METHOD = intPreferencesKey("calculation_method")
        val JURISTIC = intPreferencesKey("juristic")

        val NOTIFICATIONS_ALLOWED = booleanPreferencesKey("notifications_allowed")
    }

    override suspend fun setLocation(latitude: Double, longitude: Double) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOCATION_LATITUDE] = latitude
            preferences[PreferencesKeys.LOCATION_LONGITUDE] = longitude
        }
    }

    override suspend fun getLocation(): Coordinates? {
        val preferences = dataStore.data.first()
        val latitude = preferences[PreferencesKeys.LOCATION_LATITUDE]
        val longitude = preferences[PreferencesKeys.LOCATION_LONGITUDE]

        if (latitude != null && longitude != null) {
            return Coordinates(latitude, longitude)
        }

        return null
    }

    override suspend fun setCalculationMethod(calculationMethod: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CALCULATION_METHOD] = calculationMethod
        }
    }

    override suspend fun getCalculationMethod(): Int? {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.CALCULATION_METHOD]
    }

    override suspend fun setJuristic(juristic: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.JURISTIC] = juristic
        }
    }

    override suspend fun getJuristic(): Int? {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.JURISTIC]
    }

    override suspend fun setNotificationsAllowed(isNotificationsAllowed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ALLOWED] = isNotificationsAllowed
        }
    }

    override suspend fun getNotificationsAllowed(): Boolean? {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.NOTIFICATIONS_ALLOWED]
    }
}