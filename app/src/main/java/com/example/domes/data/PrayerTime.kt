package com.example.domes.data

data class PrayerTime(
    val prayerName: String,
    val icon: Any, // Replace 'Any' with the appropriate type for your icons
    val prayerTime: String,
    val isSilent: Boolean = false,
    val isPassed: Boolean = false,
    val isActive: Boolean = false,
    val onClick: () -> Unit = {}
)