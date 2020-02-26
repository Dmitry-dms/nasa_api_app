package com.dms.nasaapi.db.apod

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dms.nasaapi.model.PictureOfTheDay

@Dao
interface ApodDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)//заменить сущствующую запись, или если нет, добавить
    fun add(apod: PictureOfTheDay)

    //редактирование/обновление
    @Update
    fun update(apod: PictureOfTheDay)

    @Query("select * from APOD")
    fun getApod(): LiveData<PictureOfTheDay>
}