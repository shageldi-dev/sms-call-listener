package com.shageldi.smscalllocationlistener.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import com.shageldi.smscalllocationlistener.database.AppDatabase
import com.shageldi.smscalllocationlistener.model.req.Data
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


class MyReceiver : BroadcastReceiver() {
    var c: Context? = null
    var phone: String = ""
    override fun onReceive(context: Context, intent: Intent) {
        c = context
        try {
            phone = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString()
//            intent.extras?.keySet()?.forEach {
//                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
//            }
            val tmgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val PhoneListener: MyPhoneStateListener = MyPhoneStateListener()
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE)
        } catch (e: Exception) {
            Toast.makeText(context, "oops!", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class MyPhoneStateListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
//            if (state == 1) {
            c?.let {
               if(phone!=null && phone!="null"){
                   val dao = AppDatabase.getDatabase(it).dataDao()
                   val msg = "New Phone Call Event. Incoming Number: $phone"
                   val currentTime = Calendar.getInstance().time
                   val data = Data(
                       body = "${phone} / ${state}",
                       date_own = currentTime.toString(),
                       duration = "0",
                       latitude = "",
                       longitude = "",
                       name = "${c?.let { getContactName(it, "$phone") }}",
                       phone =  "${phone}",
                       status = "0",
                       type = "incoming_call"
                   )

                   GlobalScope.launch {
                       dao.insertSms(data)
                   }
               }
            }


//            }
        }
    }
}