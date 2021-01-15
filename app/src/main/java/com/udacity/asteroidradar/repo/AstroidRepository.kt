package com.udacity.asteroidradar.repo

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AstroidApi
import com.udacity.asteroidradar.api.AstroidApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AstroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AstroidRepository(private val db:AstroidDatabase) {
    var todayAstroid:LiveData<List<Asteroid>> = db.asteroidDao.getAstroidToday(getToday())
    var AstroidWeekly:LiveData<List<Asteroid>> = db.asteroidDao.getAstroidWeek(getToday(),getDaysLater(7))
    var allAstroid:LiveData<List<Asteroid>> = db.asteroidDao.getAllAstroid()

    private fun getToday(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    private fun getDaysLater(later: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, later)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }


    suspend fun insertAstroidFromNet() {
        withContext(Dispatchers.IO) {

            val res =AstroidApi.retrofitService.getProperty(getToday(),getDaysLater(Constants.DEFAULT_END_DATE_DAYS),Constants.api_key)
           val parse = parseAsteroidsJsonResult(JSONObject( res))
            insertToDB(parse)
        }
    }

    private suspend fun insertToDB (astroids:ArrayList<Asteroid>) {
        for (item in astroids) {
            db.asteroidDao.insert(item)
        }
    }
}