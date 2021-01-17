package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AstroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(asteroid: AstroidEntity)

   // @Update
   // suspend fun update(asteroid: Asteroid)

    @Query("SELECT * FROM ASTROID_TABLE ORDER BY closeApproachDate")
     fun getAllAstroid(): LiveData<List<AstroidEntity>>

    @Query("SELECT * FROM astroid_table WHERE closeApproachDate = :today")
     fun getAstroidToday(today:String):LiveData<List<AstroidEntity>>

    @Query("SELECT * FROM ASTROID_TABLE where closeApproachDate between :startDay and :endDay order by closeApproachDate desc")
     fun getAstroidWeek(startDay: String,endDay: String):LiveData<List<AstroidEntity>>


}