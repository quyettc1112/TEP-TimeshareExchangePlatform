package com.example.tep_timeshareexchangeplatform.API.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity.NotificationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Date

class FCMNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Kiểm tra nếu message chứa data payload
        if (remoteMessage.data.size > 0) {
            val title = remoteMessage.data["title"]
            val content = remoteMessage.data["content"]
            makeNotification(title, content)
            Log.d("CheckMessageRespone", (remoteMessage.data.toString()))
        }
        // Kiểm tra nếu message chứa notification payload
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val content = remoteMessage.notification!!.body
            makeNotification(title, content)
        }
    }

    private val notificationId: Int
        get() = Date().time.toInt()

    fun makeNotification(messageTitle: String? ,messageBody: String?) {
        val channelID = "CHANNEL_ID_NOTIFICATION"

        // Tạo Intent cho NotificationActivity (thay Notification bằng tên Activity mong muốn)
        val intent = Intent(this, NotificationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("message", messageBody) // Truyền dữ liệu nếu cần
        }

        // Tạo PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.logo_tep_app)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Gắn PendingIntent vào thông báo

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager.getNotificationChannel(channelID)
            if (notificationChannel == null) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                notificationChannel = NotificationChannel(channelID, "Notification Channel", importance)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        notificationManager.notify(notificationId, builder.build())
    }
}