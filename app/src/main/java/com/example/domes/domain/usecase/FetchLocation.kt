package com.example.domes.domain.usecase

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.repository.IUserPreferencesRepository
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FetchLocation(
    private val userPreferencesRepository: IUserPreferencesRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val context: Context
) {
    suspend operator fun invoke(): Coordinates? = run {
        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            return null
        }

        return fetchLocation(fusedLocationProviderClient)
    }

    @SuppressLint("MissingPermission")
    private suspend fun fetchLocation(fusedLocationProviderClient: FusedLocationProviderClient): Coordinates? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    runBlocking {
                        val coordinates = saveLocation(location)
                        continuation.resume(coordinates)
                    }
                } else {
                    continuation.resume(null)
                }
            }.addOnFailureListener {
                continuation.resume(null)
            }
        }
    }

    private suspend fun saveLocation(result: Location): Coordinates {
        val coordinates = Coordinates(result.latitude, result.longitude)
        userPreferencesRepository.saveLocation(result.latitude, result.longitude)

        return coordinates
    }

}