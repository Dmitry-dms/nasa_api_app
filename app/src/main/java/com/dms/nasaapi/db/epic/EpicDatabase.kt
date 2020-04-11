package com.dms.nasaapi.db.epic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dms.nasaapi.model.epic.Epic

@Database(entities = [Epic::class], version = 1)
abstract class EpicDatabase : RoomDatabase() {

    abstract fun getEpicDao(): EpicDAO

    companion object {
        @Volatile
        private var INSTANCE: EpicDatabase? = null

        fun getInstance(context: Context): EpicDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                EpicDatabase::class.java, "EpicDB.db"
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}