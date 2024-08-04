package com.lakehead.lakeheadsocial

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class NotificationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle notifications
        return START_STICKY
    }

    private fun sendNotification(message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("New Notification")
            .setContentText(message)
            .setSmallIcon(R.drawable.notification_icon)
            .build()
        notificationManager.notify(0, notification)
    }
}
