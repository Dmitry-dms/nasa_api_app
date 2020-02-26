package com.dms.nasaapi.db.marsRoverPhotos

import android.content.Context
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dms.nasaapi.model.MarsPhoto

@Database(entities = [MarsPhoto::class], version = 1, exportSchema = false)
abstract class MrpDatabase : RoomDatabase() {

    abstract fun getMrpDao(): MrpDAO

    companion object {
        @Volatile
        private var INSTANCE: MrpDatabase? = null

        fun getInstance(context: Context): MrpDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE?: buildDatabase(context).also { INSTANCE = it }
            }


        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MrpDatabase::class.java, "MrpDB.db"
            ).build()
    }
}