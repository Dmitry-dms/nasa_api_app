package com.dms.nasaapi.model.mrp

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "MarsPhoto")
data class MarsPhoto(
    @PrimaryKey @field:SerializedName("id") var id: Long,
    @field:SerializedName("earth_date") var earthDate: String,
    @field:SerializedName("img_src") var image: String?
)

data class MrpSearchResponse(
    @SerializedName("photos") val photos: List<MarsPhoto> = emptyList(),
    val nextPage: Int? = null
)