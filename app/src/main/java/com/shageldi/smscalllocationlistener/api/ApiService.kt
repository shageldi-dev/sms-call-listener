package com.shageldi.smscalllocationlistener.api

import com.shageldi.smscalllocationlistener.model.req.SendData
import com.shageldi.smscalllocationlistener.model.res.SendDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("write-multiple")
    fun writeMultiple(@Body() body: SendData): Call<SendDataResponse>
}