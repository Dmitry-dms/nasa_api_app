package com.dms.nasaapi.data.image_library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.model.image_library.Data
import com.dms.nasaapi.model.image_library.ImageLibrarySearchResponse
import com.dms.nasaapi.model.image_library.Item
import com.dms.nasaapi.model.image_library.Link
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImLDataSource(val query: String) : PageKeyedDataSource<Int, Item>() {
    companion object {
        var lastRequestedPage = 1
    }
    var isEmpty = true
    private val emptyness = MutableLiveData<String>()
    val em : LiveData<String>
    get() = emptyness
//    private val data:Data=Data("","0000","","")
//    private val dataList: List<Data> = arrayListOf(data)
//    private val emptyLinkList: List<Link> = emptyList()
//
//    private val item: Item = Item(dataList, emptyLinkList)
    private val emptyList: List<Item> = emptyList()
    private var isRequestInProgress = false

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        val service = ImageApiLibraryService.create()
        val call = service.getImageBySearch(query, lastRequestedPage)
        call.enqueue(object : Callback<ImageLibrarySearchResponse> {
            override fun onFailure(call: Call<ImageLibrarySearchResponse>, t: Throwable) {
                emptyness.postValue("true")
                isRequestInProgress = false
                Log.d("TAG2", "load initial response error")
                callback.onResult(emptyList, null, null)
            }

            override fun onResponse(
                call: Call<ImageLibrarySearchResponse>,
                response: Response<ImageLibrarySearchResponse>
            ) {
                if (response.isSuccessful) {
                    emptyness.postValue("false")
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.collection.items
                    Log.d("TAG2", "load initial")
                    responseItems?.let {
                        callback.onResult(
                            responseItems,
                            null,
                            lastRequestedPage + 1
                        )

                        isRequestInProgress = false
                    }
                }
            }

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        val service = ImageApiLibraryService.create()
        val call = service.getImageBySearch(query, params.key)
        call.enqueue(object : Callback<ImageLibrarySearchResponse> {
            override fun onFailure(call: Call<ImageLibrarySearchResponse>, t: Throwable) {
                emptyness.postValue("true")
                isRequestInProgress = false
                callback.onResult(emptyList, null)
                Log.d("TAG2", "load after res error")
            }

            override fun onResponse(
                call: Call<ImageLibrarySearchResponse>,
                response: Response<ImageLibrarySearchResponse>
            ) {
                if (response.isSuccessful) {
                    emptyness.postValue("false")
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.collection.items

                    val key =
                        if (apiResponse.collection.items.size > params.key) params.key + 1 else 0
                    Log.d("TAG2", "load after")
                    responseItems?.let {
                        callback.onResult(responseItems, key)
                        isRequestInProgress = false
                    }

                }
            }

        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        val service = ImageApiLibraryService.create()
        val call = service.getImageBySearch(query, params.key)
        call.enqueue(object : Callback<ImageLibrarySearchResponse> {
            override fun onFailure(call: Call<ImageLibrarySearchResponse>, t: Throwable) {
                isEmpty=true
                isRequestInProgress = false
                callback.onResult(emptyList, null)
                Log.d("TAG2", "load before response error")
            }

            override fun onResponse(
                call: Call<ImageLibrarySearchResponse>,
                response: Response<ImageLibrarySearchResponse>
            ) {
                if (response.isSuccessful) {
                    isEmpty=false
                    Log.d("TAG2", "load before")
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.collection.items

                    val key = if (params.key > 1) params.key - 1 else 0
                    responseItems?.let {
                        callback.onResult(responseItems, key)
                        isRequestInProgress = false
                    }

                }
            }

        })
    }
}