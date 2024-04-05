package com.example.domes.domain.usecase

import android.content.Context
import android.location.Geocoder
import com.batoulapps.adhan2.Coordinates
import java.util.Locale

// TODO: Rename to something like `getLocale` and include country since it's required in UI
class GetCity(
    private val context: Context
) {
    operator fun invoke(coordinates: Coordinates): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
        return addresses?.firstOrNull()?.countryName ?: "Unknown"
    }
}