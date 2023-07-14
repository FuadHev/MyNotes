package com.fuadhev.mynotes.root

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application() {

    companion object {
        const val CHANNEL_ID = "0"
        const val CHANNEL_NAME = "Alarm Channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager=getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}