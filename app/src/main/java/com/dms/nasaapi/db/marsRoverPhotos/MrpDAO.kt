package com.dms.nasaapi.db.marsRoverPhotos

import androidx.paging.DataSource
import androidx.room.*
import com.dms.nasaapi.model.mrp.MarsPhoto


@Dao
interface MrpDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(marsPhoto: List<MarsPhoto>)

    @Update
    fun update(marsPhoto: MarsPhoto)

//    @Query("select * from MarsPhoto")
//    fun getAllMarsPhotos():LiveData<List<MarsPhoto>>

    @Query("select * from MarsPhoto") //where id >= :id limit :size
    fun getAllMarsPhotos():DataSource.Factory<Int, MarsPhoto>


//    @Query("SELECT MAX(indexInResponse)+1 FROM MarsPhoto WHERE img_src =:image")
//    fun getNextIndexInImage(image: String):Int
}