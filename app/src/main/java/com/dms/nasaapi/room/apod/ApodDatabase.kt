package com.dms.nasaapi.room.apod

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dms.nasaapi.model.PictureOfTheDay
import com.dms.nasaapi.room.apod.ApodDAO

@Database(entities = [PictureOfTheDay::class],version = 2)
abstract class ApodDatabase: RoomDatabase() {

    abstract fun getApodDao(): ApodDAO



}