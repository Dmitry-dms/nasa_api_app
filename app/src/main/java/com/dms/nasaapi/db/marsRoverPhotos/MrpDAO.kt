package com.dms.nasaapi.db.marsRoverPhotos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dms.nasaapi.model.mrp.MarsPhoto


@Dao
interface MrpDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marsPhoto: List<MarsPhoto>)

    @Query("select * from MarsPhoto")
    fun getAllMarsPhotos(): DataSource.Factory<Int, MarsPhoto>

}