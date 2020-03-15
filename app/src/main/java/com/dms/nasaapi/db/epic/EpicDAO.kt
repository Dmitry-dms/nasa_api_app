package com.dms.nasaapi.db.epic

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dms.nasaapi.model.PictureOfTheDay
import com.dms.nasaapi.model.epic.Epic
@Dao
interface EpicDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)//заменить сущствующую запись, или если нет, добавить
    suspend fun add(epic: List<Epic>)

    //редактирование/обновление
    @Update
    fun update(epic: List<Epic>)

    @Query("select * from Epic")
    fun getEpic(): DataSource.Factory<Int,Epic>
}