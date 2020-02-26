package com.dms.nasaapi.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class MrpSearchResult(
    val data: LiveData<PagedList<MarsPhoto>>,
    val networkErrors: LiveData<String>
)