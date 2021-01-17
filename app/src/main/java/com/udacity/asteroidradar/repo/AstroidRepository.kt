package com.udacity.asteroidradar.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AstroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asAstroidEntity
import com.udacity.asteroidradar.database.AstroidDatabase
import com.udacity.asteroidradar.database.AstroidEntity
import com.udacity.asteroidradar.database.convertToAstroifDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AstroidRepository(private val db:AstroidDatabase) {
    var todayAstroid:LiveData<List<Asteroid>> = Transformations.map(db.asteroidDao.getAstroidToday(getToday())) {
        it.convertToAstroifDataClass()
    }
    var AstroidWeekly:LiveData<List<Asteroid>> = Transformations.map(db.asteroidDao.getAstroidWeek(getToday(),getDaysLater(7))){
        it.convertToAstroifDataClass()
    }
    var allAstroid:LiveData<List<Asteroid>> = Transformations.map(db.asteroidDao.getAllAstroid()){
        it.convertToAstroifDataClass()
    }

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

            val res =AstroidApi.retrofitService.getProperty(getToday(),getDaysLater(Constants.DEFAULT_END_DATE_DAYS),
                BuildConfig.NASA_API_KEY)
            val parse = parseAsteroidsJsonResult(JSONObject( res))

            insertToDB(parse.asAstroidEntity())
        }
    }

    private suspend fun insertToDB (astroids:List<AstroidEntity>) {
        for (item in astroids) {
            db.asteroidDao.insert(item)
        }
    }
}