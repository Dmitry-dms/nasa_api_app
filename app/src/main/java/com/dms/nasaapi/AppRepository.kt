package com.dms.nasaapi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.PictureOfTheDay
import com.dms.nasaapi.db.apod.ApodDAO
import com.dms.nasaapi.db.apod.ApodDatabase
import com.dms.nasaapi.db.marsRoverPhotos.MrpDAO
import com.dms.nasaapi.db.marsRoverPhotos.MrpDatabase

class AppRepository(application: Application) {
    private var instance: ApodDatabase? = null
    private var apodDAO: ApodDAO? = null

    init {
        if (instance == null) {
            instance = Room.databaseBuilder(
                application.applicationContext,
                ApodDatabase::class.java,
                "ApodDB.db"
            )
                .fallbackToDestructiveMigration() //когда меняем номер версии БД будет удалена и создана заного
                .allowMainThreadQueries()
                .build()
        }
        apodDAO = instance?.getApodDao()


    }

    fun getApod(): LiveData<PictureOfTheDay>? {
        return apodDAO?.getApod()
    }

    fun updateApod(pod: PictureOfTheDay) {
        apodDAO?.update(pod)
    }

    fun addApod(pod: PictureOfTheDay) {
        apodDAO?.add(pod)
    }

}