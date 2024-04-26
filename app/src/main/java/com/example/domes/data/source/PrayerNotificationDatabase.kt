package com.example.domes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.domes.domain.model.prayer.Notification
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerNotification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrayerNotificationConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromPrayer(prayer: Prayer): String {
        return gson.toJson(prayer)
    }

    @TypeConverter
    fun toPrayer(prayerString: String): Prayer {
        return gson.fromJson(prayerString, Prayer::class.java)
    }

    @TypeConverter
    fun fromList(value: List<Notification>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Notification>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toList(value: String): List<Notification> {
        val gson = Gson()
        val type = object : TypeToken<List<Notification>>() {}.type
        return gson.fromJson(value, type)
    }
}

@Database(
    entities = [PrayerNotification::class],
    version = 1
)
@TypeConverters(PrayerNotificationConverters::class)
abstract class PrayerNotificationDatabase: RoomDatabase() {
    abstract val prayerNotificationDao: PrayerNotificationDao

    companion object {
        const val DATABASE_NAME = "prayer_notification"
    }
}