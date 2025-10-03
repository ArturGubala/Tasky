package com.example.tasky.agenda.domain.notification

interface NotificationScheduler {
    fun scheduleNotification(item: Notification)
    fun cancelNotification(itemId: String)
    fun cancelNotifications()
}
