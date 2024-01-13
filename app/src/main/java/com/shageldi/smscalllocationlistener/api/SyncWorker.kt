package com.shageldi.smscalllocationlistener.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.shageldi.smscalllocationlistener.database.AppDatabase
import com.shageldi.smscalllocationlistener.model.req.SendData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val dao = AppDatabase.getDatabase(appContext).dataDao()
        val req = ApiClient.apiService
        GlobalScope.launch {
            val errored = dao.getSms()
            sendData(SendData(errored), req, dao)
        }
        return Result.success()
    }
}