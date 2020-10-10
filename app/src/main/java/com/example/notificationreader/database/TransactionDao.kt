package com.example.notificationreader.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(vararg transaction: TransactionEntity)

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): List<TransactionEntity>
}