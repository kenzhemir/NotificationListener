package com.example.notificationreader.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastReceiver(val onNotificationReceived: () -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.notificationreader.NEW_NOTIFICATION") {
            onNotificationReceived()
        }
    }

}
