package com.dms.nasaapi.api

import com.dms.nasaapi.model.MarsPhoto
import com.google.gson.annotations.SerializedName

data class MrpSearchResponse(
    @SerializedName("photos") val photos: List<MarsPhoto> = emptyList(),
    val nextPage: Int? = null
)