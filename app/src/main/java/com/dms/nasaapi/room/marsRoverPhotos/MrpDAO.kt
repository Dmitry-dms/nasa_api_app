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

    @Query("select * from MarsPhoto where id >= :id limit :size")
    fun getAllMarsPhotos(id: Int,size:Int):List<MarsPhoto>


//    @Query("SELECT MAX(indexInResponse)+1 FROM MarsPhoto WHERE img_src =:image")
//    fun getNextIndexInImage(image: String):Int
}