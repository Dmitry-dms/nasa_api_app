package com.dms.nasaapi.model.mrp

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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
    @PrimaryKey @field:SerializedName("id") var id: Long,
    @field:SerializedName("earth_date") var earthDate: String,
    @field:SerializedName("img_src") var image: String?
//    val camera: Camera,
//    val rover: Rover

)


data class Camera(
    val full_name: String

)