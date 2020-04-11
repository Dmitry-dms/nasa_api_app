package com.dms.nasaapi.model.epic


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Epic")
data class Epic(
    @SerializedName("caption")
    val caption: String,
    @SerializedName("date")
    @ColumnInfo(name = "full_date")
    var date: String,
    @SerializedName("identifier")
    @PrimaryKey
    val identifier: String,
    @SerializedName("image")
    val image: String,
    var year: String,
    var month: String,
    var day: String
)
