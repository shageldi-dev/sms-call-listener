package com.shageldi.smscalllocationlistener.database.sms

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sms_table")
data class SmsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val contactName: String?,
    val phoneNumber: String,
    val messageBody: String
)
