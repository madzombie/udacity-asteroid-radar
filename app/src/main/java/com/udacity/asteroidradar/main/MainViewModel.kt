package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AstroidApi.retrofitService
import com.udacity.asteroidradar.api.AstroidApiService
import com.udacity.asteroidradar.database.AstroidDatabase
import com.udacity.asteroidradar.repo.AstroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception


enum class AsteroidApiStatus {LOADING, ERROR, DONE}
enum class ApiFilter { WEEK, DAY, ALL }

class MainViewModel (application: Application): AndroidViewModel(application) {
    private val _image = MutableLiveData<PictureOfDay>()

    val image: LiveData<PictureOfDay>
        get() = _image

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    val navigateToSelectedAsteroid : LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid
   // private val database = AstroidDatabase.getInstance(application)
    private val database = AstroidDatabase.getDatabase(application)

    private  val repo = AstroidRepository(database)

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    init {
        viewModelScope.launch {
            _status.value= AsteroidApiStatus.LOADING
            try {
                repo.insertAstroidFromNet()
                _status.value= AsteroidApiStatus.DONE
                getPictureOfDay()
            } catch (e:Exception) {
                _status.value= AsteroidApiStatus.ERROR
            }
        }
    }

    private val _databaseAsteroidList = MutableLiveData<ApiFilter>(ApiFilter.WEEK)
    val databaseAsteroidList = Transformations.switchMap(_databaseAsteroidList) { range->
        when(range) {

            ApiFilter.DAY->repo.todayAstroid
            ApiFilter.ALL->repo.allAstroid
            else-> repo.AstroidWeekly
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            //
            try{
                val pic = retrofitService.getPicOfDay(Constants.api_key)
                _image.value=pic
            } catch (e:Exception){
                e.printStackTrace()

            }
            //
        }
    }

    fun update(range:ApiFilter) {
        _databaseAsteroidList.value=range
    }

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

}