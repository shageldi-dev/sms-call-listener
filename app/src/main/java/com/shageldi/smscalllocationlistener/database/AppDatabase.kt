package com.shageldi.smscalllocationlistener.database

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase;
import com.shageldi.smscalllocationlistener.database.sms.DataDao
import com.shageldi.smscalllocationlistener.database.sms.SmsDao
import com.shageldi.smscalllocationlistener.database.sms.SmsEntity
import com.shageldi.smscalllocationlistener.model.req.Data

@Database(entities = [SmsEntity::class, Data::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao
    abstract fun dataDao(): DataDao

    companion object {
        // You may want to use a singleton pattern to ensure only one instance of the database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sms_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
