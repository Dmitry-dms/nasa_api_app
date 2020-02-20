package com.dms.nasaapi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
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
   // indices = [Index(value = ["image"],unique = false)])
data class MarsPhoto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int =0,

    @ColumnInfo(name = "earth_date")
    @SerializedName("earth_date")
    var earthDate: String,
    @ColumnInfo(name = "img_src")
    @SerializedName("img_src")
    var image: String
//    val camera: Camera,
//    val rover: Rover

){
    var indexInResponse: Int = -1
}


data class Camera(
    val full_name: String

)