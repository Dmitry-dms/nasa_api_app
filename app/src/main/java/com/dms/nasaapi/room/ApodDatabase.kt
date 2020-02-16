package com.dms.nasaapi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dms.nasaapi.api.RetrofitClient.instance
import com.dms.nasaapi.model.PictureOfTheDay

@Database(entities = [PictureOfTheDay::class],version = 2)
abstract class ApodDatabase: RoomDatabase() {

    abstract fun getApodDao():ApodDAO



}