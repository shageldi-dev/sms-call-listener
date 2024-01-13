package com.shageldi.smscalllocationlistener.database.sms

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shageldi.smscalllocationlistener.model.req.Data

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSms(data: Data)

    @Query("SELECT * FROM data")
    suspend fun getSms(): List<Data>

    @Delete
    suspend fun delete(data: Data)
}