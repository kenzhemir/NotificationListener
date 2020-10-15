package com.example.notificationreader.notifications

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.preference.PreferenceManager


class NotificationListener : NotificationListenerService() {

    val TAG = this::class.simpleName

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.apply {
            val appToListen = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                .getString("app_to_listen", applicationContext.packageName)

            if (packageName == appToListen || packageName == applicationContext.packageName) {
                val i = Intent("com.example.notificationreader.NEW_NOTIFICATION")

                NotificationHandler(applicationContext) { sendBroadcast(i) }.handleNotification(
                    notification
                )

            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
    }

}