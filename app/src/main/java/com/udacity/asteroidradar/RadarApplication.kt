package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.repo.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class RadarApplication :Application() {
    companion object {
        lateinit var application: Application
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    fun backgorundThread() {
        CoroutineScope(Default).launch {
            loadingData()
        }
    }
    fun loadingData() {
        val constranintVal  =Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).setRequiresCharging(true).build()
        val reqVal = PeriodicWorkRequestBuilder<RefreshDataWorker>(1,TimeUnit.DAYS).setConstraints(
            constranintVal
        ).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME,ExistingPeriodicWorkPolicy.KEEP,reqVal)
    }
}