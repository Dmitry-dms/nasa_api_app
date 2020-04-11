package com.dms.nasaapi.model.apod

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "APOD")
data class PictureOfTheDay(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "explanation")
    val explanation: String,
    @ColumnInfo(name = "picture_url")
    @SerializedName("url")
    val picture: String,
    @ColumnInfo(name = "title")
    val title: String
)

