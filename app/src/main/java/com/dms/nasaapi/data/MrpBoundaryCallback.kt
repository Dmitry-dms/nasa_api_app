package com.dms.nasaapi.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.searchMarsPhotos
import com.dms.nasaapi.db.marsRoverPhotos.MrpLocalCache
import com.dms.nasaapi.model.MarsPhoto

class MrpBoundaryCallback(
    private val query: String,
    private val service: NasaApiService,
    private val cache: MrpLocalCache
) : PagedList.BoundaryCallback<MarsPhoto>() {
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        searchMarsPhotos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { photos ->
            cache.insert(photos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })

    }

    override fun onZeroItemsLoaded() {
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: MarsPhoto) {
        requestAndSaveData(query)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}