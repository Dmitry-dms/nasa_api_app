package com.dms.nasaapi.data.image_library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.api.searchImageLibrary
import com.dms.nasaapi.model.image_library.Item

class ImLBoundaryCallback(private val query: String,
                          private val service: ImageApiLibraryService) : PagedList.BoundaryCallback<Item>() {
    companion object {
        // keep the last requested page. When the request is successful, increment the page number.
        var lastRequestedPage = 1
    }

    init {
        Log.d("TAG2", "callback")
    }
    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()
    private val _isEmpty = MutableLiveData<Boolean>()

    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    override fun onItemAtFrontLoaded(itemAtFront: Item) {
        Log.d("TAG2", "front")
    }

    override fun onZeroItemsLoaded() {
        Log.d("TAG2", "zero")
        requestData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Item) {
        Log.d("TAG2", "end")
        requestData(query)
    }
    private fun requestData(query: String) {
        Log.d("TAG2", "request data")
        if (isRequestInProgress) return
        isRequestInProgress = true
        searchImageLibrary(service,query,lastRequestedPage,{items ->
//            if (items.isEmpty()) _isEmpty.postValue(false)
//            else _isEmpty.postValue(true)
            Log.d("TAG2", "suc")
        },{error ->
            Log.d("TAG2", "err")
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }


}