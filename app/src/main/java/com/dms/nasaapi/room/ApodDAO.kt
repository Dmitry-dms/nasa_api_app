package com.dms.nasaapi.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dms.nasaapi.model.PictureOfTheDay

@Dao
interface ApodDAO {

    @Insert
    fun add(apod: PictureOfTheDay):Long

    //редактирование/обновление
    @Update
    fun update(apod: PictureOfTheDay)

    @Query("select * from APOD")
    fun getApod(): LiveData<PictureOfTheDay>
}