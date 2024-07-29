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
import java.time.LocalDateTime

@HiltWorker
class PrayerTimesUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val alarmService: AlarmService,
    private val useCases: PrayerUseCases,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // TODO: Might need to do extra work for prayers the next day depending of time of day called
        try {
            Prayer.ALL.forEach(alarmService::cancel)

            val currentTime = LocalDateTime.now()
            val coordinates = useCases.getLocation()
            val parameters = (useCases.getCalculationMethod()
                ?: CalculationMethod.MUSLIM_WORLD_LEAGUE).parameters

            coordinates?.let {
                val (prayerTimes, _) = useCases.getPrayerTimes(
                    currentTime.toLocalDate(),
                    it,
                    parameters
                )

                useCases.updatePrayerTimes(prayerTimes.onEach { prayerTime ->
                    if (currentTime.isBefore(prayerTime.time)) {
                        alarmService.schedule(prayerTime)
                    }
                }
                )
            }
        } catch (e: Exception) {
            e.message?.let { Log.e("PRAYER-TIMES-UPDATE-WORKER", it) }
            return Result.failure()
        }
        return Result.success()
    }
}