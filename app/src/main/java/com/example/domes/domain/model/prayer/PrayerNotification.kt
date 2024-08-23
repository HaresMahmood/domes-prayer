package com.example.domes.domain.model.prayer

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

enum class AlertType {
    SILENT,
    VIBRATE,
    SOUND,
    SOUND_AND_VIBRATE,
}

enum class Sound {
    DEVICE_DEFAULT,
}

@Parcelize
data class Notification(
    var label: String = "",
    var offset: Int = 0,
    var alertType: AlertType = AlertType.SOUND_AND_VIBRATE,
    var sound: Sound = Sound.DEVICE_DEFAULT,
) : Parcelable

@Entity
data class PrayerNotification(
    @PrimaryKey
    val prayer: Prayer,
    var isNotificationAllowed: Boolean = true,
    val notifications: MutableList<Notification> = mutableListOf(
        Notification(label = "Default", alertType = AlertType.SOUND)
    ),
)