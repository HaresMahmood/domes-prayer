package com.example.domes.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domes.domain.model.PrayerRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PrayerRecordDao {
    @Query("SELECT * FROM prayerRecord")
    fun getAll(): Flow<List<PrayerRecord>>

    @Query("SELECT * FROM prayerrecord WHERE date = :date")
    suspend fun getByDate(date: LocalDate): PrayerRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: PrayerRecord)

    @Delete
    suspend fun delete(record: PrayerRecord)
}