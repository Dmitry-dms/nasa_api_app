package com.dms.nasaapi.db.epic

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dms.nasaapi.model.epic.Epic

@Dao
interface EpicDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(epic: List<Epic>?)

    @Query("select * from Epic WHERE full_date ==:date")
    fun getEpic(date: String): DataSource.Factory<Int, Epic>
}