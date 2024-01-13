package com.shageldi.smscalllocationlistener.model.req

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val body: String,
    val date_own: String,
    val duration: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val phone: String,
    val status: String,
    val type: String,

)