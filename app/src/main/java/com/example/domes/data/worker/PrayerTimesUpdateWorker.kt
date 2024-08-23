package com.example.domes.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.batoulapps.adhan2.CalculationMethod
import com.example.domes.data.service.AlarmService
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.domain.usecase.PrayerUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDateTime

@HiltWorker
class PrayerTimesUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val alarmService: AlarmService,
    private val useCases: PrayerUseCases,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        try {
            val prayerNotifications = useCases.getPrayerNotifications().firstOrNull().orEmpty()

            Prayer.ALL.forEach { prayer ->
                prayerNotifications.find { it.prayer == prayer }?.notifications?.forEach {
                    alarmService.cancel(prayer, it)
                } ?: alarmService.cancel(prayer)
            }

            useCases.getLocation()?.let { coordinates ->
                val currentTime = LocalDateTime.now()
                val parameters = useCases.getCalculationMethod()?.parameters
                    ?: CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
                val today = currentTime.toLocalDate()
                val prayerTimes = listOf(today, today.plusDays(1)).flatMap { date ->
                    useCases.getPrayerTimes(date, coordinates, parameters).first
                }

                println("All prayer times: $prayerTimes")

                useCases.updatePrayerTimes(prayerTimes.filter {
                    it.time.toLocalDate() == today
                })

                val filteredPrayerTimes = prayerTimes.filter {
                    it.time.isAfter(currentTime) && it.time.isBefore(currentTime.plusDays(1))
                }

                filteredPrayerTimes.groupBy { it.prayer }
                    .forEach { (prayer, times) ->
                        useCases.getPrayerNotification(prayer)?.takeIf { it.isNotificationAllowed }
                            ?.let { notification ->
                                notification.notifications.forEach {
                                    alarmService.schedule(
                                        times.first(),
                                        it
                                    )
                                }
                            } ?: alarmService.schedule(times.first())
                    }
            }
        } catch (e: Exception) {
            e.message?.let { Log.e("PRAYER-TIMES-UPDATE-WORKER", it) }
            return Result.failure()
        }
        return Result.success()
    }
}