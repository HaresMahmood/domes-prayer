package com.example.domes.data.source

import PrayerRecord
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PrayerRecord::class],
    version = 1
)
abstract class PrayerRecordDatabase: RoomDatabase() {
    abstract val prayerRecordDao: PrayerRecordDao
}