package com.example.domes.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.domes.domain.model.prayer.PrayerTime
import com.example.domes.domain.usecase.PrayerUseCases
import com.example.domes.presentation.routes.prayers.UIConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {
    @Inject
    lateinit var prayersUseCases: PrayerUseCases

    override fun onReceive(context: Context, intent: Intent?) {
        val prayerTime = intent?.getParcelableExtra<PrayerTime>("PRAYERTIME") ?: return
//        val locale = prayersUseCases.getLocale(coordinates)

        prayersUseCases.sendNotification(
            prayerTime,
            "At ${prayerTime.time.format(UIConstants.getFormatter("HH:mm a"))}",
            context
        )
    }
}