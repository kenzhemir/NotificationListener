package com.example.notificationreader.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
class TransactionEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var text: String
)