@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tasky.agenda.domain.notification.Notification
import com.example.tasky.agenda.domain.notification.NotificationScheduler
import com.example.tasky.agenda.presentation.util.toKotlinInstant
import java.time.ZonedDateTime
import kotlin.time.ExperimentalTime

class NotificationSchedulerImpl(
    private val context: Context,
) : NotificationScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleNotification(item: Notification) {
        val currentTime = System.currentTimeMillis()

        if (item.remindAt <= currentTime) {
            return
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("ITEM_ID", item.id)
            putExtra("ITEM_KIND", item.kind.toString())
            putExtra("TITLE", item.title)
            putExtra("DESCRIPTION", item.description ?: "")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            ZonedDateTime.now().plusSeconds(30).toKotlinInstant().toEpochMilliseconds(),
            pendingIntent
        )
    }

    override fun cancelNotification(itemId: String) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            itemId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    override fun cancelNotifications(itemIds: List<String>) {
        itemIds.forEach { itemId ->
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                itemId.hashCode(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            pendingIntent?.let {
                alarmManager.cancel(it)
                it.cancel()
            }
        }
    }
}
