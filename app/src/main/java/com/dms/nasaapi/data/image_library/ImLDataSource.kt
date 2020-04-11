package com.dms.nasaapi.data.image_library

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.model.NetworkState
import com.dms.nasaapi.model.image_library.ImageLibrarySearchResponse
import com.dms.nasaapi.model.image_library.Item
import kotlinx.coroutines.*

class ImLDataSource(val query: String, val api: ImageApiLibraryService) :
    PageKeyedDataSource<Int, Item>() {


    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob) //coroutine scope
    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        coroutineScope.launch {
            try {
                networkState.postValue(NetworkState.LOADING)
                val response = api.getImageBySearch(query, 1)
                if (response.collection.items.isNotEmpty()) {
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(response.collection.items, null,2)
                } else {
                    retry = {
                        loadInitial(params, callback)
                    }
                    networkState.postValue(NetworkState.EMPTY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                retry = {
                    loadInitial(params, callback)
                }
                networkState.postValue(NetworkState.error("Network error"))
            }

        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val response = api.getImageBySearch(query, params.key)
                if (response.collection.items.isNotEmpty()) {
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(response.collection.items, params.key.inc())
                } else {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.EMPTY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error("Network error"))
            }

        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {

    }


    fun clearCoroutineJobs() {
        completableJob.cancel()
    }
}