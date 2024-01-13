package com.shageldi.smscalllocationlistener.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.shageldi.smscalllocationlistener.database.AppDatabase
import com.shageldi.smscalllocationlistener.model.req.Data
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


class OutgoingCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val dao = AppDatabase.getDatabase(context).dataDao()
        Log.d(OutgoingCallReceiver::class.java.simpleName, intent.toString())
        val phone = intent.getStringExtra("android.intent.extra.PHONE_NUMBER")
        val currentTime = Calendar.getInstance().time
        val data = Data(
            body = "${phone}",
            date_own = currentTime.toString(),
            duration = "0",
            latitude = "",
            longitude = "",
            name = "${getContactName(context, "$phone")}",
            phone =  "${phone}",
            status = "0",
            type = "outgoing_call"
        )

        GlobalScope.launch {
            dao.insertSms(data)
        }
    }
}