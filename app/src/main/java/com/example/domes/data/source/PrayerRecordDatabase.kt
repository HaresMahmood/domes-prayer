package com.example.domes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.domes.domain.model.prayer.PrayerRecord
import java.time.LocalDate


class PrayerRecordConverters {
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
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
        const val DATABASE_NAME = "prayer_records"
    }
}