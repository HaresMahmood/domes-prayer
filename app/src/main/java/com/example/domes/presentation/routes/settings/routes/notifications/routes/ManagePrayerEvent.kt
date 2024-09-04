package com.example.domes.presentation.routes.settings.routes.notifications.routes

import com.example.domes.domain.model.prayer.Notification
import com.example.domes.domain.model.prayer.Prayer

sealed class ManagePrayerEvent {
    data class FetchPrayerData(val prayer: Prayer) : ManagePrayerEvent()
    data class ChangeNotificationsAllowed(val isNotificationAllowed: Boolean? = null) :
        ManagePrayerEvent()

    data class ChangeExpandedNotification(val notificationIndex: Int?) : ManagePrayerEvent()
    data object AddNotification : ManagePrayerEvent()
    data class RemoveNotification(val notification: Notification) : ManagePrayerEvent()
    data class ChangeOffset(val notification: Notification, val increment: Int) :
        ManagePrayerEvent()

    data class ToggleOffsetPolarity(val notification: Notification) : ManagePrayerEvent()
}
