package com.dms.nasaapi.model.image_library

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class ImLSearchResult(
    val data: LiveData<PagedList<Item>>,
    val networkErrors: LiveData<String>?,
    val isEmpty:LiveData<Boolean>?
)