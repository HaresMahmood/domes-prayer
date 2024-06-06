package com.example.domes.di

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.domes.data.repository.OldPrayerTimesRespitory
import com.example.domes.data.repository.PrayerNotificationRepository
import com.example.domes.data.repository.PrayerRecordRepository
import com.example.domes.data.repository.PrayerTimesRepository
import com.example.domes.data.repository.UserPreferencesRepository
import com.example.domes.data.service.AlarmService
import com.example.domes.data.service.NotificationService
import com.example.domes.data.source.PrayerNotificationDatabase
import com.example.domes.data.source.PrayerRecordDatabase
import com.example.domes.data.source.PrayerTimesDatabase
import com.example.domes.domain.repository.IPrayerRecordRepository
import com.example.domes.domain.repository.IUserPreferencesRepository
import com.example.domes.domain.usecase.AddPrayerNotification
import com.example.domes.domain.usecase.AddPrayerRecord
import com.example.domes.domain.usecase.FetchLocation
import com.example.domes.domain.usecase.GetCalculationMethod
import com.example.domes.domain.usecase.GetCurrentPrayer
import com.example.domes.domain.usecase.GetCurrentPrayerTimes
import com.example.domes.domain.usecase.GetDayFraction
import com.example.domes.domain.usecase.GetHijriDate
import com.example.domes.domain.usecase.GetJuristic
import com.example.domes.domain.usecase.GetLocale
import com.example.domes.domain.usecase.GetLocation
import com.example.domes.domain.usecase.GetNotificationsAllowed
import com.example.domes.domain.usecase.GetPrayerNotification
import com.example.domes.domain.usecase.GetPrayerNotifications
import com.example.domes.domain.usecase.GetPrayerRecord
import com.example.domes.domain.usecase.GetPrayerTime
import com.example.domes.domain.usecase.GetPrayerTimeLeft
import com.example.domes.domain.usecase.GetPrayerTimes
import com.example.domes.domain.usecase.GetQiblaDirection
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.domain.usecase.SendNotification
import com.example.domes.domain.usecase.SetCalculationMethod
import com.example.domes.domain.usecase.SetJuristic
import com.example.domes.domain.usecase.SetLocation
import com.example.domes.domain.usecase.SetNotificationsAllowed
import com.example.domes.domain.usecase.UpdatePrayerTimes
import com.example.domes.presentation.routes.settings.routes.calculation.CalculationState
import com.example.domes.presentation.routes.settings.routes.location.LocationState
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

    @Provides
    @Singleton
    fun providePrayerTimesDatabase(app: Application): PrayerTimesDatabase {
        return Room.databaseBuilder(
            app,
            PrayerTimesDatabase::class.java,
            PrayerTimesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayerTimesRepository(database: PrayerTimesDatabase): PrayerTimesRepository {
        return PrayerTimesRepository(database.prayerTimesDao)
    }

    @Provides
    @Singleton
    fun providePrayerNotificationDatabase(app: Application): PrayerNotificationDatabase {
        return Room.databaseBuilder(
            app,
            PrayerNotificationDatabase::class.java,
            PrayerNotificationDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayerNotificationRepository(database: PrayerNotificationDatabase): PrayerNotificationRepository {
        return PrayerNotificationRepository(database.prayerNotificationDao)
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
    fun provideAlarmService(app: Application): AlarmService {
        return AlarmService(app)
    }

    @Provides
    @Singleton
    fun provideLocationState(): MutableState<LocationState> {
        return mutableStateOf(LocationState())
    }

    @Provides
    @Singleton
    fun provideCalculationState(): MutableState<CalculationState> {
        return mutableStateOf(CalculationState())
    }

    @Provides
    @Singleton
    fun providePrayerUseCases(
        prayerRecordRepository: IPrayerRecordRepository,
        prayerTimesRepository: PrayerTimesRepository,
        prayerNotificationRepository: PrayerNotificationRepository,
        userPreferencesRepository: IUserPreferencesRepository,
        notificationService: NotificationService,
        fusedLocationProviderClient: FusedLocationProviderClient,
        app: Application
    ): PrayerUseCases {
        val oldPrayerTimesRepository = OldPrayerTimesRespitory()

        return PrayerUseCases(
            getPrayerTimes = GetPrayerTimes(oldPrayerTimesRepository),
            getCurrentPrayerTimes = GetCurrentPrayerTimes(prayerTimesRepository),
            updatePrayerTimes = UpdatePrayerTimes(prayerTimesRepository),
            getCurrentPrayer = GetCurrentPrayer(),
            getPrayerTime = GetPrayerTime(prayerTimesRepository),
            getPrayerRecord = GetPrayerRecord(prayerRecordRepository),
            addPrayerRecord = AddPrayerRecord(prayerRecordRepository),
            getPrayerNotification = GetPrayerNotification(prayerNotificationRepository),
            getPrayerNotifications = GetPrayerNotifications(prayerNotificationRepository),
            addPrayerNotification = AddPrayerNotification(prayerNotificationRepository),
            getHijriDate = GetHijriDate(),
            getPrayerTimeLeft = GetPrayerTimeLeft(),
            sendNotification = SendNotification(notificationService),
            getNotificationsAllowed = GetNotificationsAllowed(userPreferencesRepository),
            setNotificationsAllowed = SetNotificationsAllowed(userPreferencesRepository),
            getLocale = GetLocale(app),
            fetchLocation = FetchLocation(userPreferencesRepository, fusedLocationProviderClient, app),
            setLocation = SetLocation(userPreferencesRepository),
            getLocation = GetLocation(userPreferencesRepository),
            setJuristic = SetJuristic(userPreferencesRepository),
            getJuristic = GetJuristic(userPreferencesRepository),
            setCalculationMethod = SetCalculationMethod(userPreferencesRepository),
            getCalculationMethod = GetCalculationMethod(userPreferencesRepository),
            getQiblaDirection = GetQiblaDirection(),
            getDayFraction = GetDayFraction(),
        )
    }
}