package com.dms.nasaapi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dms.nasaapi.api.RetrofitClient.instance
import com.dms.nasaapi.model.PictureOfTheDay

@Database(entities = [PictureOfTheDay::class],version = 1)
abstract class ApodDatabase: RoomDatabase() {

    abstract fun getApodDao():ApodDAO
    private var instance:ApodDatabase? = null


//     fun getInstance(context: Context): ApodDatabase? {
//        if (instance == null) {
//            instance = Room.databaseBuilder(
//                    context.applicationContext,
//            ApodDatabase::class.java, "ApodDB"
//            )
//            .fallbackToDestructiveMigration() //когда меняем номер версии БД будет удалена и создана заного
//                .build()
//        }
//        return instance
//    }

}