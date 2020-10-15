package com.example.notificationreader.notifications

import android.app.Notification
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.notificationreader.database.TransactionDb
import com.example.notificationreader.database.TransactionEntity
import com.example.notificationreader.utils.NamedRegex

class NotificationHandler(var ctx: Context, private val onDbSave: () -> Unit) {

    private val regexes: List<Regex>
        get() = PreferenceManager.getDefaultSharedPreferences(ctx)
            .getStringSet(
                "notification_text_regexes", setOf(
                    """(?<amount>\d*.?\d*) EUR покупка (?<payee>.*)""",
                    """Совершён платеж на (?<amount>\d*.?\d*) EUR на имя (?<payee>.*)""",
                    """Получателю (?<payee>.*) было отправлено (?<amount>\d*.?\d*) EUR"""
                )
            )?.map { Regex(it) } ?: listOf()


    fun saveToDB(transactionEntity: TransactionEntity) {
        TransactionDb.getInstance(ctx).getTransactionDao()
            .insertTransactions(transactionEntity)
        onDbSave()
    }

    fun handleNotification(notification: Notification) {
        val nText = notification.extras.getString(Notification.EXTRA_TEXT) ?: ""

        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
        val accountId = prefs.getString("ynab_account_id", null)
        val budgetId = prefs.getString("ynab_budget_id", null)
        val accessToken = prefs.getString("ynab_access_token", null)

        if (accountId != null && budgetId != null && accessToken != null)
            regexes.mapNotNull { NamedRegex.extractNamedGroups(it, nText) }.firstOrNull()?.let {
                YnabTransactionApi(ctx, accountId, budgetId, accessToken).sendRequest(it, {
                    saveToDB(TransactionEntity(0, nText, true))
                }, {
                    saveToDB(TransactionEntity(0, nText, false))
                })
            }
    }
}