package com.example.domes.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.domes.data.repository.PrayerRecordRepository
import com.example.domes.data.repository.PrayerTimesRespitory
import com.example.domes.data.repository.UserPreferencesRepository
import com.example.domes.data.service.NotificationService
import com.example.domes.data.source.PrayerRecordDatabase
import com.example.domes.domain.repository.IPrayerRecordRepository
import com.example.domes.domain.repository.IUserPreferencesRepository
import com.example.domes.domain.usecase.AddPrayerRecord
import com.example.domes.domain.usecase.FetchLocation
import com.example.domes.domain.usecase.GetCity
import com.example.domes.domain.usecase.GetCurrentPrayer
import com.example.domes.domain.usecase.GetDayFraction
import com.example.domes.domain.usecase.GetHijriDate
import com.example.domes.domain.usecase.GetLocation
import com.example.domes.domain.usecase.GetPrayerRecord
import com.example.domes.domain.usecase.GetPrayerTimeLeft
import com.example.domes.domain.usecase.GetPrayerTimes
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.domain.usecase.SendNotification
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile("user_preferences") }
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): IUserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        app: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)

    @Provides
    @Singleton
    fun provideNotificationService(app: Application): NotificationService {
        return NotificationService(app)
    }

    @Provides
    @Singleton
    fun providePrayerUseCases(
        prayerRecordRepository: IPrayerRecordRepository,
        userPreferencesRepository: IUserPreferencesRepository,
        notificationService: NotificationService,
        fusedLocationProviderClient: FusedLocationProviderClient,
        app: Application
    ): PrayerUseCases {
        val prayerTimesRepository = PrayerTimesRespitory()

        return PrayerUseCases(
            getPrayerTimes = GetPrayerTimes(prayerTimesRepository),
            getCurrentPrayer = GetCurrentPrayer(),
            getPrayerRecord = GetPrayerRecord(prayerRecordRepository),
            addPrayerRecord = AddPrayerRecord(prayerRecordRepository),
            getHijriDate = GetHijriDate(),
            getPrayerTimeLeft = GetPrayerTimeLeft(),
            sendNotification = SendNotification(notificationService),
            getCity = GetCity(app),
            fetchLocation = FetchLocation(userPreferencesRepository, fusedLocationProviderClient, app),
            getLocation = GetLocation(userPreferencesRepository),
            getDayFraction = GetDayFraction(),
        )
    }
}