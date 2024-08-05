package com.lakehead.lakeheadsocial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class NotificationService : Service() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var initialLoadComplete = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationService", "Service started")
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        createNotificationChannel()
        startListeningForChanges()

        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "channel_id",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            Log.d("NotificationService", "Notification channel created")
        }
    }

    private fun startListeningForChanges() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = db.collection("users").document(currentUser.uid)

            // Listen for new followers
            userRef.addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    Log.e("NotificationService", "Error listening for followers: ${error?.message}")
                    return@addSnapshotListener
                }
                val user = snapshot.toObject(User::class.java) ?: return@addSnapshotListener
                val newFollowerCount = user.followers.size

                Log.d("NotificationService", "Current follower count: $newFollowerCount")
                // Check if the number of followers has increased
                if (newFollowerCount > oldFollowerCount && initialLoadComplete) {
                    sendNotification("New Follower", "You have a new follower!")
                    oldFollowerCount = newFollowerCount
                }

                // Listen for new posts from followed users
                if (!initialLoadComplete) {
                    for (followedUserId in user.following) {
                        db.collection("posts")
                            .whereEqualTo("userId", followedUserId)
                            .addSnapshotListener { snapshots, e ->
                                if (e != null || snapshots == null) {
                                    Log.e("NotificationService", "Error listening for new posts: ${e?.message}")
                                    return@addSnapshotListener
                                }

                                for (dc in snapshots.documentChanges) {
                                    if (dc.type == DocumentChange.Type.ADDED && initialLoadComplete) {
                                        Log.d("NotificationService", "New post by followed user: $followedUserId")
                                        sendNotification("New Post", "Someone you follow has made a new post!")
                                    }
                                }
                            }
                    }
                    initialLoadComplete = true
                }
            }
        }
    }

    private fun sendNotification(title: String, message: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), notification)

        Log.d("NotificationService", "Notification sent: $title - $message")
    }

    companion object {
        private var oldFollowerCount = 0
    }
}
