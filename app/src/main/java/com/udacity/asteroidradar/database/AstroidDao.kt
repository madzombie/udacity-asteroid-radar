package com.udacity.asteroidradar.database

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AstroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(asteroid: Asteroid)

    @Update
    suspend fun update(asteroid: Asteroid)

    @Query("SELECT * FROM ASTROID_TABLE ORDER BY close_approach_date")
     fun getAllAstroid(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM astroid_table WHERE close_approach_date = :today")
     fun getAstroidToday(today:String):LiveData<List<Asteroid>>

    @Query("SELECT * FROM ASTROID_TABLE where close_approach_date between :startDay and :endDay order by close_approach_date desc")
     fun getAstroidWeek(startDay: String,endDay: String):LiveData<List<Asteroid>>

    @Query("DELETE FROM ASTROID_TABLE")
     fun clear()

}