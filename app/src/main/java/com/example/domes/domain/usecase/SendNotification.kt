package com.example.domes.domain.usecase

import com.example.domes.data.service.NotificationService
import com.example.domes.domain.model.service.Notification

class SendNotification(
    private val notificationService: NotificationService
) {
    operator fun invoke(params: Notification) {
        notificationService.sendNotification(params)
    }
}