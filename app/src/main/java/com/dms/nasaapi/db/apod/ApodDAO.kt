package com.dms.nasaapi.db.apod

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dms.nasaapi.model.apod.PictureOfTheDay

@Dao
interface ApodDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(apod: PictureOfTheDay)

    @Query("select * from APOD")
    fun getApod(): LiveData<PictureOfTheDay>
}