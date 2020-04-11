package com.dms.nasaapi.data.mrp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dms.nasaapi.api.MrpApiService
import com.dms.nasaapi.db.marsRoverPhotos.MrpDAO


import com.dms.nasaapi.model.NetworkState
import com.dms.nasaapi.model.mrp.MarsPhoto
import kotlinx.coroutines.*
import java.lang.Exception

class MrpBoundaryCallback(
    private val query: String,
    private val service: MrpApiService,
    private val dao: MrpDAO
) : PagedList.BoundaryCallback<MarsPhoto>() {

    private var lastRequestedPage = 1
    private var retry: (() -> Any)? = null
    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    private val _networkState = MutableLiveData<NetworkState>()

    val networkState: LiveData<NetworkState>
        get() = _networkState


    private fun requestAndSaveData(query: String) {

        coroutineScope.launch(Dispatchers.IO) {
            try {
                _networkState.postValue(NetworkState.LOADING)
                val response = service.getMarsPhoto(lastRequestedPage, NETWORK_PAGE_SIZE)
                if (response.photos.isNotEmpty()) {
                    dao.insert(response.photos)
                    _networkState.postValue(NetworkState.LOADED)
                    lastRequestedPage++

                } else {
                    _networkState.postValue(NetworkState.EMPTY)
                }
            } catch (e: Exception) {
                _networkState.postValue(NetworkState.error("Network error"))
                e.printStackTrace()

                retry = {
                    requestAndSaveData(query)
                }
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
        completableJob.cancel()
    }
}