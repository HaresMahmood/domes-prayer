package com.example.domes.domain.model.location


data class Locale(
    val city: String?,
    val country: String?,
    val countryCode: String?,
    val state: String?
) {
    companion object {
        fun unknown(): Locale {
            return Locale(null, null, null, null)
        }
    }
}

fun Locale.isUnknown(): Boolean {
    return listOfNotNull(
        city, country, countryCode, state
    ).all { it.isEmpty() }
}