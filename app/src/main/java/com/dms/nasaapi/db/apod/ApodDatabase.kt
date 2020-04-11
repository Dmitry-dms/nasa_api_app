package com.dms.nasaapi.db.apod

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dms.nasaapi.model.apod.PictureOfTheDay

@Database(entities = [PictureOfTheDay::class],version = 1)
abstract class ApodDatabase: RoomDatabase() {

    abstract fun getApodDao(): ApodDAO

    companion object {
        @Volatile
        private var INSTANCE: ApodDatabase? = null

        fun getInstance(context: Context): ApodDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ApodDatabase::class.java, "ApodDB.db"
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

}