package com.shageldi.smscalllocationlistener.api

import com.shageldi.smscalllocationlistener.database.sms.DataDao
import com.shageldi.smscalllocationlistener.model.req.Data
import com.shageldi.smscalllocationlistener.model.req.SendData
import com.shageldi.smscalllocationlistener.model.res.SendDataResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun sendData(data: SendData, apiService: ApiService, dao: DataDao){
    val call = apiService.writeMultiple(data)
    call.enqueue(object : Callback<SendDataResponse> {
        override fun onResponse(call: Call<SendDataResponse>, response: Response<SendDataResponse>) {
            if (response.isSuccessful) {
                println("Successfully sent")
                data.data.forEach {
                    GlobalScope.launch {
                        dao.delete(it)
                    }
                }
            } else {
            }
        }

        override fun onFailure(call: Call<SendDataResponse>, t: Throwable) {
            t.printStackTrace()

        }
    })
}