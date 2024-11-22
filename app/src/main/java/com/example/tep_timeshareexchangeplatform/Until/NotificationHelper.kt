package com.example.tep_timeshareexchangeplatform.Until

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tep_timeshareexchangeplatform.R


class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "default_channel_id"
        private const val CHANNEL_NAME = "Default Channel"
        private const val CHANNEL_DESCRIPTION = "This is the default notification channel"
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(notificationId: Int, title: String, message: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Nếu quyền chưa được cấp, yêu cầu quyền
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
            return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_unwind_logo_25)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun makeNotification(context: Context, title: String, message: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Nếu quyền chưa được cấp, yêu cầu quyền
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
            return
        }



        val channelID = "CHANNEL_ID_NOTIFICATION"
        val builder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_unwind_logo_25)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager.getNotificationChannel(channelID)
            if (notificationChannel == null) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                notificationChannel = NotificationChannel(channelID, "Some description", importance).apply {
                    lightColor = Color.GREEN
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        notificationManager.notify(getNotificationId(), builder.build())
    }

    private fun getNotificationId(): Int {
        return System.currentTimeMillis().toInt()
    }



    // Hủy thông báo theo ID
    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    // Hủy tất cả các thông báo
    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }
}