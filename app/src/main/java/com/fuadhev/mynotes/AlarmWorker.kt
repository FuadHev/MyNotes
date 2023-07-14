package com.fuadhev.mynotes

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fuadhev.mynotes.root.App.Companion.CHANNEL_ID
import com.fuadhev.mynotes.ui.view.activity.MainActivity


/**  This codes not using  **/
class AlarmWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val alarmMessage = inputData.getString("ALARM_MESSAGE_KEY")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {


            val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText(alarmMessage)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(null, true)

//            val notificationIntent = Intent(applicationContext, MainActivity::class.java)
//            val pendingIntent = PendingIntent.getActivity(
//                applicationContext,
//                0,
//                notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//            notificationBuilder.setContentIntent(pendingIntent)

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        } else {
            // Android Oreo öncesi sürümlerde bildirim oluşturma işlemi
            // Bu sürümlerde bildirimler ekran kapalıyken görüntülenemez
        }

        return Result.success()
    }

    companion object {
//        private const val CHANNEL_ID = "alarm_channel"
        private const val NOTIFICATION_ID = 1
    }
}