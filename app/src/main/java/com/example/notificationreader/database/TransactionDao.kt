package com.example.notificationreader.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransactions(vararg transaction: TransactionEntity): List<Long>

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): List<TransactionEntity>

    @Query("UPDATE transactions SET is_saved = 1 WHERE id = :transaction_id")
    fun markAsSaved(transaction_id: Long)
}