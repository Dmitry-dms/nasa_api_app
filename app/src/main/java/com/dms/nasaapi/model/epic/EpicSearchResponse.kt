package com.dms.nasaapi.model.epic

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dms.nasaapi.model.image_library.NetworkState

data class EpicSearchResponse(
    val data: LiveData<PagedList<Epic>>,
    val networkErrors: LiveData<NetworkState>
)