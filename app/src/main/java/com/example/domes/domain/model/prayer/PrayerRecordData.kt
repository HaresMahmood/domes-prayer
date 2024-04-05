package com.example.domes.domain.model.prayer

enum class MissedReason {
    FORGOT, SLEPT_THROUGH, SICK, TRAVELING, PERIOD, PREGNANT, POSTPARTUM, BREASTFEEDING, INCONTINENCE, OTHER
}

data class PrayerRecordData(
    val prayed: Boolean = false,
    val timestamp: Long? = null,
    val notes: String? = null,
    val missed: Boolean = false,
    val missedReason: MissedReason? = null,
    val makeup: Boolean = false
)
