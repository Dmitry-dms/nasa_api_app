package com.dms.nasaapi.room.marsRoverPhotos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.MarsRoverPhoto

@Database(entities = [MarsPhoto::class],version = 1)
abstract class MrpDatabase :RoomDatabase() {

    abstract fun getMrpDao(): MrpDAO
}