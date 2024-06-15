package com.example.domes.domain.usecase

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.batoulapps.adhan2.Coordinates
import com.example.domes.domain.model.location.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetLocale(private val context: Context) {
    suspend operator fun invoke(coordinates: Coordinates): Locale {
        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= 33) {
                    geocodeUsingModernApi(coordinates)
                } else {
                    geocodeUsingLegacyApi(coordinates)
                }
            } catch (e: IOException) {
                Locale.unknown()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun geocodeUsingModernApi(coordinates: Coordinates): Locale {
        return suspendCoroutine { continuation ->
            val geocoder = Geocoder(context, java.util.Locale.getDefault())
            geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1) { addresses ->
                val locale = extractLocationFromAddress(addresses.firstOrNull())

                Log.d("GetLocale", "Modern API - Locale: $locale, Addresses: $addresses")

                continuation.resume(locale)
            }
        }
    }

    private fun geocodeUsingLegacyApi(coordinates: Coordinates): Locale {
        val geocoder = Geocoder(context, java.util.Locale.getDefault())
        val addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)

        Log.d("GetLocale", "Legacy API - Addresses: $addresses")

        return extractLocationFromAddress(addresses?.firstOrNull())
    }

    private fun extractLocationFromAddress(address: Address?): Locale {
        return address?.let {
            Locale(
                city = it.locality ?: it.subAdminArea ?: it.adminArea,
                country = it.countryName,
                countryCode = it.countryCode,
                state = it.adminArea
            )
        } ?: Locale.unknown()
    }
}