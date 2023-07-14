package com.fuadhev.mynotes.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fuadhev.mynotes.AlarmWorker
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.root.App
import com.fuadhev.mynotes.ui.view.activity.MainActivity

class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmMessage = intent?.getStringExtra("ALARM_MESSAGE_KEY")?: "Reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {


            val notificationBuilder = NotificationCompat.Builder(context!!, App.CHANNEL_ID)
                .setContentTitle("MyNotes Reminder")
                .setContentText(alarmMessage)
                .setSmallIcon(R.mipmap.ic_launcher_mynotes)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(null, true)




            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notificationBuilder.build())
        } else {
            // Android Oreo öncesi sürümlerde bildirim oluşturma işlemi
            // Bu sürümlerde bildirimler ekran kapalıyken görüntülenemez
        }
    }
}