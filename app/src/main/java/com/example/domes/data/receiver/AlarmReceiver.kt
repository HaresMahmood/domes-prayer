package com.example.domes.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {
    @Inject
    lateinit var useCases: PrayerUseCases

    override fun onReceive(context: Context?, intent: Intent?) {
        val prayerTime = intent?.getParcelableExtra<PrayerTime>("PRAYERTIME") ?: return
//        val locale = prayersUseCases.getLocale(coordinates)

        println("ALARM: RECEIVED IN BROADCAST RECEIVER for ${prayerTime}")

        if (context != null) {
            useCases.sendNotification(
                prayerTime,
                "From ${prayerTime.time.format(getFormatter("HH:mm a"))} to ${prayerTime.timeWindow.format(getFormatter("HH:mm a"))}",
                context
            )
        }
    }
}