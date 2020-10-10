package com.example.notificationreader

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notificationreader.database.TransactionDb
import com.example.notificationreader.database.TransactionEntity


class NotificationListener : NotificationListenerService() {

    init {
        Log.i("MIRAS", "init")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        Log.i("MIRAS", sbn.toString())
        sbn?.apply {
            if (packageName == "com.swedbank") {
                val i = Intent("com.example.notificationreader.NEW_NOTIFICATION")
                sendBroadcast(i)
                notification.extras.getString(Notification.EXTRA_TEXT)?.let {
                    TransactionDb.getInstance(applicationContext).getTransactionDao()
                        .addTransaction(
                            TransactionEntity(0, it)
                        )
                }
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        Log.i("MIRAS", "notification removed")
    }

}