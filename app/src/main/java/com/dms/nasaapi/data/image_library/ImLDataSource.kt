package com.dms.nasaapi.data.image_library

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.model.image_library.ImageLibrarySearchResponse
import com.dms.nasaapi.model.image_library.Item
import com.dms.nasaapi.model.image_library.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImLDataSource(val query: String, val api: ImageApiLibraryService) :
    PageKeyedDataSource<Int, Item>() {


    private var isRequestInProgress = false
    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob) //coroutine scope
    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()

    val isEmpty = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        coroutineScope.launch{
            try {
                networkState.postValue(NetworkState.LOADING)
                val primarySearchResponse = callLatestSearch(query, 1)
                primarySearchResponse.enqueue(object : Callback<ImageLibrarySearchResponse> {
                    override fun onFailure(call: Call<ImageLibrarySearchResponse>, t: Throwable) {
                        networkState.postValue(NetworkState.error("Network error"))
                    }

                    override fun onResponse(
                        call: Call<ImageLibrarySearchResponse>,
                        response: Response<ImageLibrarySearchResponse>
                    ) {
                        if (response.isSuccessful) {
                            retry = null
                            networkState.postValue(NetworkState.LOADED)
                            if (response.body()!!.collection.items.isEmpty()){
                                isEmpty.postValue(NetworkState.EMPTY)
                                callback.onResult(response.body()!!.collection.items, null,2)
                            } else {
                                isEmpty.postValue(NetworkState.FULL)
                                callback.onResult(response.body()!!.collection.items, null, 2)
                            }
                        } else {
                            retry = {
                                loadInitial(params, callback)
                            }
                            networkState.postValue(NetworkState.error("Network error"))
                        }
                    }
                })

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
                networkState.postValue(NetworkState.LOADING)
                val primarySearchResponse = callLatestSearch(query, params.key)
                primarySearchResponse.enqueue(object : Callback<ImageLibrarySearchResponse> {
                    override fun onFailure(call: Call<ImageLibrarySearchResponse>, t: Throwable) {
                        networkState.postValue(NetworkState.error("Network error"))
                    }

                    override fun onResponse(
                        call: Call<ImageLibrarySearchResponse>,
                        response: Response<ImageLibrarySearchResponse>
                    ) {
                        if (response.isSuccessful) {
                            retry = null
                            networkState.postValue(NetworkState.LOADED)
                            callback.onResult(response.body()!!.collection.items, params.key.inc())
                        } else {
                            retry = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(NetworkState.error("Network error"))
                        }
                    }
                })

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

    private suspend fun callLatestSearch(
        query: String,
        page: Int
    ): Call<ImageLibrarySearchResponse> = api.getImageBySearch(query, page)

    fun clearCoroutineJobs() {
        completableJob.cancel()
    }
}