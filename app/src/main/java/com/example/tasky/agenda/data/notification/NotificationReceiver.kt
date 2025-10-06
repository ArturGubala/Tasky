package com.example.tasky.agenda.data.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.tasky.R
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.app.MainActivity
import com.example.tasky.core.presentation.util.AGENDA_DETAIL_DEEPLINK_PATH
import com.example.tasky.core.presentation.util.DEEPLINK_DOMAIN

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val itemId = intent.getStringExtra("ITEM_ID") ?: return
        val kind = intent.getStringExtra("ITEM_KIND") ?: return
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
            data =
                "https://$DEEPLINK_DOMAIN/$AGENDA_DETAIL_DEEPLINK_PATH/$kind/${AgendaDetailView.EDIT}?agendaId=$itemId".toUri()
        }

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(detailIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

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
