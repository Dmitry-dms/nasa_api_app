package com.dms.nasaapi.data.mrp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.db.marsRoverPhotos.MrpDAO


import com.dms.nasaapi.model.image_library.NetworkState
import com.dms.nasaapi.model.mrp.MarsPhoto
import kotlinx.coroutines.*
import java.lang.Exception

class MrpBoundaryCallback(
    private val query: String,
    private val service: NasaApiService,
    private val dao: MrpDAO
) : PagedList.BoundaryCallback<MarsPhoto>() {
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1
    private var retry: (() -> Any)? = null
    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }
    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<NetworkState>()
    // LiveData of network errors.
    val networkErrors: LiveData<NetworkState>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return
        coroutineScope.launch(Dispatchers.IO) {
            isRequestInProgress = true
            try{
                val response = service.getMarsPhoto(lastRequestedPage, NETWORK_PAGE_SIZE)
                if (response!=null){
                    dao.insert(response.photos)
                    _networkErrors.postValue(NetworkState.LOADED)
                    lastRequestedPage++
                    isRequestInProgress = false
                } else{
                    _networkErrors.postValue(NetworkState.EMPTY)
                    isRequestInProgress = false
                }
            }catch (e:Exception){
                _networkErrors.postValue(NetworkState.error("smth went wrong!"))
                e.printStackTrace()
                isRequestInProgress = false
            }
        }
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
    fun clearCoroutineJob() {
        coroutineScope.cancel()
    }
}