package com.example.domes.domain.repository

import com.example.domes.domain.model.PrayerRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IPrayerRecordRepository {
    fun getAll(): Flow<List<PrayerRecord>>

    suspend fun getByDate(date: LocalDate): PrayerRecord?

    suspend fun insert(record: PrayerRecord)

    suspend fun delete(record: PrayerRecord)
}