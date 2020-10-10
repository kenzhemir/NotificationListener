package com.example.notificationreader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver(val mainActivity: MainActivity) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.notificationreader.NEW_NOTIFICATION") {
            mainActivity.updateNotifications()
        }
    }

}
