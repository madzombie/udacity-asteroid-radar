package com.udacity.asteroidradar.repo

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.CoroutineWorker
import com.udacity.asteroidradar.database.AstroidDatabase
import retrofit2.HttpException


class LoadWorker (context: Context,params:WorkerParameters):
    CoroutineWorker(context,params) {
    companion object {
        const val WORK_NAME="LoadWorker"
    }
    override suspend fun doWork():Result {
        val dbName= AstroidDatabase.getInstance(applicationContext)
        val repo = AstroidRepository(dbName)
        return try{
            repo.insertAstroidFromNet()
            Result.success()
        } catch (e:HttpException) {
            Result.retry()
        }
    }
}
