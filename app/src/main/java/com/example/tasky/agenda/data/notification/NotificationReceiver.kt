package com.example.tasky.agenda.data.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.tasky.R
import com.example.tasky.app.MainActivity

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val itemId = intent.getStringExtra("ITEM_ID") ?: return
        val kind = intent.getStringExtra("ITEM_KIND") ?: ""
        val title = intent.getStringExtra("TITLE") ?: "Notification"
        val description = intent.getStringExtra("DESCRIPTION") ?: ""

        showNotification(context, itemId, title, description, kind)
    }

    private fun showNotification(
        context: Context,
        itemId: String,
        title: String,
        description: String,
        kind: String,
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val detailIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("AGENDA_ITEM_ID", itemId)
            putExtra("AGENDA_ITEM_TYPE", kind)
        }

        val pendingIntent = android.app.PendingIntent.getActivity(
            context,
            itemId.hashCode(),
            detailIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "reminders")
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(itemId.hashCode(), notification)
    }
}
