package com.dms.nasaapi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.dms.nasaapi.model.PictureOfTheDay
import com.dms.nasaapi.room.ApodDAO
import com.dms.nasaapi.room.ApodDatabase

class AppRepository(application: Application) {
    private var instance:ApodDatabase? = null
    private var apodDAO: ApodDAO? = null
    private var pod: LiveData<PictureOfTheDay>? =null

    init{
        if (instance == null) {
            instance = Room.databaseBuilder(application.applicationContext, ApodDatabase::class.java, "ApodDB")
                .fallbackToDestructiveMigration() //когда меняем номер версии БД будет удалена и создана заного
                .allowMainThreadQueries()
                .build()
        }
        apodDAO = instance?.getApodDao()
    }
    fun getApod(): LiveData<PictureOfTheDay>? {
        return apodDAO?.getApod()
    }
    fun updateApod(pod: PictureOfTheDay){
        apodDAO?.update(pod)
    }
    fun addApod(pod: PictureOfTheDay){
        apodDAO?.add(pod)
    }

}