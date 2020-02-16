package com.dms.nasaapi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.PictureOfTheDay
import com.dms.nasaapi.room.apod.ApodDAO
import com.dms.nasaapi.room.apod.ApodDatabase
import com.dms.nasaapi.room.marsRoverPhotos.MrpDAO
import com.dms.nasaapi.room.marsRoverPhotos.MrpDatabase

class AppRepository(application: Application) {
    private var instance: ApodDatabase? = null
    private var apodDAO: ApodDAO? = null

    private var mrpInstance: MrpDatabase? = null
    private var mrpDAO: MrpDAO? = null

    init{
        if (instance == null) {
            instance = Room.databaseBuilder(application.applicationContext, ApodDatabase::class.java, "ApodDB")
                .fallbackToDestructiveMigration() //когда меняем номер версии БД будет удалена и создана заного
                .allowMainThreadQueries()
                .build()
        }
        apodDAO = instance?.getApodDao()

        if (mrpInstance ==null){
            mrpInstance = Room.databaseBuilder(application.applicationContext, MrpDatabase::class.java, "MrpDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
        mrpDAO = mrpInstance?.getMrpDao()


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

    fun getAllPhoto(): LiveData<List<MarsPhoto>>?{
        return mrpDAO?.getAllMarsPhotos()
    }
    fun addMrp(marsPhoto: List<MarsPhoto>){
        marsPhoto.forEach { mrpDAO?.add(it) }

    }

}