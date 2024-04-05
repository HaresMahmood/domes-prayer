package com.example.domes.di

import android.app.Application
import androidx.room.Room
import com.example.domes.data.repository.PrayerRecordRepository
import com.example.domes.data.service.NotificationService
import com.example.domes.data.source.PrayerRecordDatabase
import com.example.domes.domain.repository.IPrayerRecordRepository
import com.example.domes.domain.usecase.GetCity
import com.example.domes.domain.usecase.GetHijriDate
import com.example.domes.domain.usecase.GetPrayerRecord
import com.example.domes.domain.usecase.GetPrayerTimeLeft
import com.example.domes.domain.usecase.GetPrayerTimes
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.domain.usecase.SendNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePrayerRecordDatabase(app: Application): PrayerRecordDatabase {
        return Room.databaseBuilder(
            app,
            PrayerRecordDatabase::class.java,
            PrayerRecordDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayerRecordRepository(database: PrayerRecordDatabase): IPrayerRecordRepository {
        return PrayerRecordRepository(database.prayerRecordDao)
    }

    @Provides
    @Singleton
    fun provideNotificationService(app: Application): NotificationService {
        return NotificationService(app)
    }

    @Provides
    @Singleton
    fun providePrayerUseCases(
        repository: IPrayerRecordRepository,
        notificationService: NotificationService,
        app: Application
    ): PrayerUseCases {
        return PrayerUseCases(
            getPrayerTimes = GetPrayerTimes(),
            getPrayerRecord = GetPrayerRecord(repository),
            getHijriDate = GetHijriDate(),
            getPrayerTimeLeft = GetPrayerTimeLeft(),
            sendNotification = SendNotification(notificationService),
            getCity = GetCity(app)
        )
    }
}