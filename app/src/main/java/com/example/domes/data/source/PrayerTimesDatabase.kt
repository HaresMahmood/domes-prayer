package com.example.domes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import com.google.gson.Gson
import java.time.LocalDateTime

class PrayerTimesConverters {
    private val gson = Gson()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromPrayer(prayer: Prayer): String {
        return gson.toJson(prayer)
    }

    @TypeConverter
    fun toPrayer(prayerString: String): Prayer {
        return gson.fromJson(prayerString, Prayer::class.java)
    }
}

@Database(
    entities = [PrayerTime::class],
    version = 1
)
@TypeConverters(PrayerTimesConverters::class)
abstract class PrayerTimesDatabase: RoomDatabase() {
    abstract val prayerTimesDao: PrayerTimesDao

    companion object {
        const val DATABASE_NAME = "prayer_time"
    }
}