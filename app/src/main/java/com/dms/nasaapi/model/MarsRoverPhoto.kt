package com.dms.nasaapi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class MarsRoverPhoto(

    val photos: List<MarsPhoto>
)

data class Rover(
    val id: Int,
    val landing_date: String,
    val launch_date: String,
    val name: String,
    val status: String,
    val total_photos: Int
)
@Entity(tableName = "MarsPhoto")
data class MarsPhoto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int =0,

    @ColumnInfo(name = "earth_date")
    @SerializedName("earth_date")
    val earthDate: String,
    @ColumnInfo(name = "img_src")
    @SerializedName("img_src")
    val image: String
//    val camera: Camera,
//    val rover: Rover

)
data class Camera(
    val full_name: String

)