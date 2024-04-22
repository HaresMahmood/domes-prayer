package com.example.domes.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.domes.domain.model.prayer.PrayerTime

class NotificationActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        intent?.getParcelableExtra<PrayerTime>("PRAYERTIME")?.let { prayerTime ->
//            _state.value.selectedPrayerRecord.takeIf { it.date == prayerTime.time.toLocalDate() }?.let {
//                onEvent(PrayersEvent.ChangePrayerRecord(prayerTime.prayer, true))
//            } ?: viewModelScope.launch {
//                prayersUseCases.getPrayerRecord(prayerTime.time.toLocalDate())?.let { fetchedPrayerRecord ->
//                    updatePrayerRecord(fetchedPrayerRecord, prayerTime.prayer, true)
//                }
//            }
//
//            // TODO: Move into use case and notification service
//            val notificationId = prayerTime.hashCode() // Use the same ID that was used to create the PendingIntent
//            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.cancel(notificationId)

            println("NOTIFICATION-ACTION: RECEIVED")
        }
    }
}