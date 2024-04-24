package com.example.domes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.domes.domain.model.prayer.PrayerRecord
import com.example.domes.domain.model.prayer.PrayerRecordDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate


class PrayerRecordConverters {
    private val gson = Gson()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromList(value: List<PrayerRecordDetail>): String {
        val gson = Gson()
        val type = object : TypeToken<List<PrayerRecordDetail>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toList(value: String): List<PrayerRecordDetail> {
        val gson = Gson()
        val type = object : TypeToken<List<PrayerRecordDetail>>() {}.type
        return gson.fromJson(value, type)
    }
}

@Database(
    entities = [PrayerRecord::class],
    version = 1
)
@TypeConverters(PrayerRecordConverters::class)
abstract class PrayerRecordDatabase: RoomDatabase() {
    abstract val prayerRecordDao: PrayerRecordDao

    companion object {
        const val DATABASE_NAME = "prayer_record"
    }
}