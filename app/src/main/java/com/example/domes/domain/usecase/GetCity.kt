package com.example.domes.domain.usecase

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.batoulapps.adhan2.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

// TODO: Rename to something like `getLocale` and include country since it's required in UI
class GetCity(
    private val context: Context
) {
    suspend operator fun invoke(coordinates: Coordinates): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var city = "Unknown city"

        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            println("GeocodeListener: $addresses")

            city = addresses[0]?.run {
                locality ?: subAdminArea ?: subLocality ?: thoroughfare ?: subThoroughfare
            } ?: "Unknown city"
        }

        if (Build.VERSION.SDK_INT >= 33) {
            geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1, geocodeListener)
        } 
        else {
            println("GeocodeListener: not supported")

            return withContext(Dispatchers.IO) {
                val addresses = geocoder.getFromLocation(
                    coordinates.latitude, coordinates.longitude, 1
                )

                city = addresses?.get(0)?.run {
                    locality ?: subAdminArea ?: subLocality ?: thoroughfare ?: subThoroughfare
                } ?: "Unknown city"
                city
            }
        }

        return city
    }
}