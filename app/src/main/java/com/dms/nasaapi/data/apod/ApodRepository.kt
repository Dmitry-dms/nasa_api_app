package com.dms.nasaapi.data.apod

import androidx.lifecycle.LiveData
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.db.apod.ApodDAO
import com.dms.nasaapi.model.PictureOfTheDay

class ApodRepository(val service: NasaApiService,val dao: ApodDAO) {
    //TODO() add WorkManager to update ApodDB every day
    fun search(): LiveData<PictureOfTheDay> {
        return dao.getApod()
    }
}