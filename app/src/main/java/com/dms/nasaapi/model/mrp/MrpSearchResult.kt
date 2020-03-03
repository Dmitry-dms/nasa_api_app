package com.dms.nasaapi.model.mrp

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dms.nasaapi.model.mrp.MarsPhoto

data class MrpSearchResult(
    val data: LiveData<PagedList<MarsPhoto>>,
    val networkErrors: LiveData<String>
)