package com.example.domes.domain.usecase

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.batoulapps.adhan2.Coordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FetchLocation(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val context: Context,
    private val setLocation: SetLocation
) {
    private val job = Job() // Create a Job to manage the scope's lifecycle
    private val scope = CoroutineScope(Dispatchers.IO + job) // Create a scope with the Job

    suspend operator fun invoke(): Coordinates? {
        val locationManager = ContextCompat.getSystemService(context, LocationManager::class.java)

        if (locationManager == null || !LocationManagerCompat.isLocationEnabled(locationManager)) {
            return null
        }

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return fetchLocation()
    }

    private suspend fun fetchLocation(): Coordinates? =
        suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationProviderClient.getCurrentLocation(
                100, cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    val coordinates = Coordinates(location.latitude, location.longitude)
                    scope.launch {
                        setLocation.invoke(coordinates)
                    }
                    continuation.resume(coordinates)
                } else {
                    continuation.resume(null)
                }
            }.addOnFailureListener {
                continuation.resume(null)
            }.addOnCanceledListener {
                continuation.cancel()
            }

            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }
}