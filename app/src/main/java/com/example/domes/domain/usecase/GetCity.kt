package com.example.domes.domain.usecase

import android.content.Context
import android.location.Geocoder
import com.batoulapps.adhan2.Coordinates
import java.util.Locale

// TODO: Rename to something like `getLocale` and include country since it's required in UI
class GetCity(
    private val context: Context
) {
    suspend operator fun invoke(coordinates: Coordinates): String {
        val geocoder = Geocoder(context, Locale.getDefault())

        // if (Build.VERSION.SDK_INT >= 33) {
        //     val geocodeListener = Geocoder.GeocodeListener { addresses ->
        //         val city = addresses[0]?.run {
        //             locality ?: subAdminArea ?: adminArea
        //         } ?: "Unknown City 1"

        //         city
        //     }

        //     geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1, geocodeListener)
        // } 
        // else {
            
        // }

        return geocoder.getFromLocation(
                coordinates.latitude, coordinates.longitude, 1
            )?.get(0)?.run {
                locality ?: subAdminArea ?: adminArea
            } ?: "Unknown City 2"

        // return withContext(Dispatchers.IO) {
        //     val addresses = Geocoder(context).getFromLocation(
        //         coordinates.latitude, coordinates.longitude, 1
        //     )
        //     val city = addresses?.get(0)?.run {
        //         locality ?: subAdminArea ?: subLocality ?: thoroughfare ?: subThoroughfare
        //     } ?: "Unknown city"

        //     city
        // }
    }
}