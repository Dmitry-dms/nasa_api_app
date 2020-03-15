package com.dms.nasaapi.ui.image_and_video

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dms.nasaapi.data.image_library.ImLRepository

import com.dms.nasaapi.model.image_library.Item
import com.dms.nasaapi.model.image_library.Listing
import com.dms.nasaapi.model.image_library.NetworkState

class ImageLibraryViewModel(private val repository: ImLRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()

    private val queryResult: LiveData<Listing<Item>> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val items: LiveData<PagedList<Item>> = Transformations.switchMap(queryResult) { it.pagedList }

    val networkState: LiveData<NetworkState> =
        Transformations.switchMap(queryResult) { it -> it.networkState }

    val refreshState: LiveData<NetworkState> =
        Transformations.switchMap(queryResult) { it.refreshState }

    val emptyState: LiveData<NetworkState> = Transformations.switchMap(queryResult) { it.isEmpty }


    fun searchImages(query: String) {
        queryLiveData.postValue(query)
    }

    fun retry() {
        queryResult.value?.retry?.invoke()
    }

    fun lastQueryValue(): String? = queryLiveData.value

    override fun onCleared() {
        super.onCleared()
        queryResult.value?.clearCoroutineJobs?.invoke()
    }

}
