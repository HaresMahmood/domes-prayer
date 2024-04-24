package com.example.domes.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.model.prayer.PrayerNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerNotificationDao {
    @Query("SELECT * FROM prayernotification")
    fun getAll(): Flow<List<PrayerNotification>>

    @Query("SELECT * FROM prayerNotification WHERE prayer = :prayer")
    suspend fun getByPrayer(prayer: Prayer): PrayerNotification?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: PrayerNotification)

    @Delete
    suspend fun delete(notification: PrayerNotification)
}