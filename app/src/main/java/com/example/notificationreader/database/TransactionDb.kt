package com.example.notificationreader.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransactionEntity::class], version = 1)
abstract class TransactionDb : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao

    companion object {
        private lateinit var transactionDb: TransactionDb

        @Synchronized
        fun getInstance(context: Context): TransactionDb {

            if (!this::transactionDb.isInitialized) {
                transactionDb = Room.databaseBuilder(
                    context, TransactionDb::class.java, "myTransactions"
                )
                    .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                    .allowMainThreadQueries() // if possible, use background thread instead
                    .build()
            }
            return transactionDb

        }
    }
}