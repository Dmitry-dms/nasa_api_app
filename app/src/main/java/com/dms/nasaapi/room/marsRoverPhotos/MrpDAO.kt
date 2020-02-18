package com.dms.nasaapi.room.marsRoverPhotos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.MarsRoverPhoto


@Dao
interface MrpDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(marsPhoto: MarsPhoto)

    @Update
    fun update(marsPhoto: MarsPhoto)

//    @Query("select * from MarsPhoto")
//    fun getAllMarsPhotos():LiveData<List<MarsPhoto>>

    @Query("select * from MarsPhoto")
    fun getAllMarsPhotos():DataSource.Factory<Int,MarsPhoto>
}