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
        return withContext(Dispatchers.IO) {
            var city: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Geocoder(context, Locale.getDefault()).getFromLocation(
                    coordinates.latitude, coordinates.longitude, 1
                )?.get(0)?.run {
                    locality ?: subAdminArea ?: subLocality ?: thoroughfare ?: subThoroughfare
                } ?: "Unknown city"
            }
            else {
                Geocoder(context).getFromLocation(
                    coordinates.latitude, coordinates.longitude, 1
                )?.get(0)?.run {
                    locality ?: subAdminArea ?: subLocality ?: thoroughfare ?: subThoroughfare
                } ?: "Unknown city"
            }.also {
                city = it
            }

            city
        }
    }
}