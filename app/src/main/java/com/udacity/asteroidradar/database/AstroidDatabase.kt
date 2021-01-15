package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [AstroidEntity::class],version = 1,exportSchema = false)
abstract class AstroidDatabase :RoomDatabase(){
    abstract val asteroidDao:AstroidDao

    companion object {
        @Volatile
        private lateinit var  INSTANCE: AstroidDatabase
            fun getInstance (context: Context):AstroidDatabase{
                synchronized(this) {
                    var instance= INSTANCE
                    if (instance==null) {
                        instance= Room.databaseBuilder(context.applicationContext,AstroidDatabase::class.java,
                        "astroid_history_database").fallbackToDestructiveMigration().build()
                        INSTANCE=instance
                    }
                    return instance
                }
            }

    }

}