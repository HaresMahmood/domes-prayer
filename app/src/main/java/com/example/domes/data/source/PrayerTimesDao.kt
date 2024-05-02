package com.example.domes.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerTime
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTimesDao {
    @Query("SELECT * FROM PrayerTime")
    fun getAll(): Flow<List<PrayerTime>>

    @Query("SELECT * FROM PrayerTime WHERE prayer = :prayer")
    suspend fun getByPrayer(prayer: Prayer): PrayerTime?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prayerTime: PrayerTime)

    @Delete
    suspend fun delete(prayerTime: PrayerTime)
}