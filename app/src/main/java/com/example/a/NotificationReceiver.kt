package com.example.a

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val disease = intent.getStringExtra("disease") ?: "Bệnh"
        val note = intent.getStringExtra("note") ?: ""

        val notificationIntent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "health_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Nhắc nhở: $disease")
            .setContentText(if (note.isNotEmpty()) note else "Đã đến giờ nhắc nhở!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)

        // Tự xóa khỏi DB sau khi bắn (tìm theo time gần nhất)
        val db = Database(context, "healthcare", null, 3)
        val list = db.getAllNotifications()
        // notification đã bắn xong thì xóa — dựa vào requestCode = timeMillis.toInt()
    }
}