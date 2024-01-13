package com.shageldi.smscalllocationlistener.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.SmsMessage
import android.util.Log
import com.shageldi.smscalllocationlistener.api.ApiClient
import com.shageldi.smscalllocationlistener.api.sendData
import com.shageldi.smscalllocationlistener.database.AppDatabase
import com.shageldi.smscalllocationlistener.database.sms.SmsEntity
import com.shageldi.smscalllocationlistener.model.req.Data
import com.shageldi.smscalllocationlistener.model.req.SendData
import com.shageldi.smscalllocationlistener.model.res.SendDataResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class SmsListener : BroadcastReceiver() {
    private val preferences: SharedPreferences? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        val dao = AppDatabase.getDatabase(context).dataDao()

        // TODO Auto-generated method stub
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras //---get the SMS message passed in---
            var msgs: Array<SmsMessage?>? = null
            var msg_from: String
            var msg_contact: String
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    val pdus = bundle["pdus"] as Array<Any>?
                    msgs = arrayOfNulls(pdus!!.size)
                    for (i in msgs.indices) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        msg_from = msgs[i]?.originatingAddress ?: "Unknown"
                        msg_contact = getContactName(context, msg_from) ?: "Unknown"
                        val msgBody: String = msgs[i]?.messageBody ?: "Message"
                        println(msgs[i])
                        println("Message: $msgBody from $msg_from $msg_contact")
                        insertSmsIntoDatabase(context, msg_contact, msg_from, msgBody)
                        val currentTime = Calendar.getInstance().time

                        val send = SendData(
                            listOf(
                                Data(body=msgBody, date_own =  currentTime.toString(), duration = "0", latitude = "", longitude = "", name = msg_contact, phone = msg_from, status = "0", type = "sms")
                            )
                        )

                        val req = ApiClient.apiService
                        req.writeMultiple(send).enqueue(object : Callback<SendDataResponse> {
                            override fun onResponse(call: Call<SendDataResponse>, response: Response<SendDataResponse>) {
                                if (response.isSuccessful) {
                                    println("Successfully sent")
                                    GlobalScope.launch {
                                        val errored = dao.getSms()
                                        sendData(SendData(errored), req, dao)
                                    }
                                } else {
                                    println("Failed to send:"+response.code())
                                    send.data.forEach {
                                        GlobalScope.launch {
                                            dao.insertSms(it)
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<SendDataResponse>, t: Throwable) {
                                t.printStackTrace()
                                send.data.forEach {
                                    GlobalScope.launch {
                                        dao.insertSms(it)
                                    }
                                }
                            }
                        })
                    }
                } catch (e: Exception) {
                    e.localizedMessage?.let { Log.d("Exception caught", it) }
                }
            }
        }
    }



    private fun insertSmsIntoDatabase(
        context: Context,
        contactName: String,
        phoneNumber: String,
        messageBody: String
    ) {
        GlobalScope.launch {
            val smsDao = AppDatabase.getDatabase(context).smsDao()
            val smsEntity = SmsEntity(contactName = contactName, phoneNumber = phoneNumber, messageBody = messageBody)
            smsDao.insertSms(smsEntity)
        }
    }
}

fun getContactName(context: Context, phoneNumber: String): String? {
    // Query the contact database to get the contact name
    val uri = Uri.withAppendedPath(
        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(phoneNumber)
    )
    val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
    var contactName: String? = ""
    try {
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                contactName = it.getString(it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
            }
        }
    } catch (e: Exception){

    }

    return contactName
}

fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    //should check null because in airplane mode it will be a null
    return netInfo != null && netInfo.isConnected
}
