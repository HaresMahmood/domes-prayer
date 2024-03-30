package com.example.domes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/*
class PrayerRecordConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromPrayerRecord(prayerRecord: PrayerRecord): String {
        return json.encodeToString(prayerRecord)
    }

    @TypeConverter
    fun toPrayerRecord(jsonString: String): PrayerRecord {
        return json.decodeFromString(jsonString)
    }
}

class MissedReasonConverter {
    @TypeConverter
    fun fromMissedReason(reason: MissedReason?): String? = reason?.name

    @TypeConverter
    fun toMissedReason(name: String?): MissedReason? = name?.let { MissedReason.valueOf(it) }
}
 */

@Entity
data class PrayerRecord(
    @PrimaryKey val date: LocalDate,
    val fajr: Boolean,
    val dhuhr: Boolean,
    val asr: Boolean,
    val maghrib: Boolean,
    val isha: Boolean
)