package com.example.notificationreader.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notificationreader.database.TransactionDb
import com.example.notificationreader.database.TransactionEntity

class TransactionsViewModel(private val context: Application) : AndroidViewModel(context) {

    var transactions: List<TransactionEntity>

    init {
        transactions = TransactionDb.getInstance(context).getTransactionDao().getAllTransactions()
    }

    fun refresh() {
        transactions = TransactionDb.getInstance(context).getTransactionDao().getAllTransactions()
    }

}