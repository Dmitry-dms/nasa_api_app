package com.dms.nasaapi.ui.image_and_video

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dms.nasaapi.data.image_library.ImLRepository

import com.dms.nasaapi.model.image_library.Item
import com.dms.nasaapi.model.Listing
import com.dms.nasaapi.model.NetworkState

class ImageLibraryViewModel(private val repository: ImLRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()

    private val queryResult: LiveData<Listing<Item>> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val items: LiveData<PagedList<Item>> = Transformations.switchMap(queryResult) { it.pagedList }

    val networkState: LiveData<NetworkState> =
        Transformations.switchMap(queryResult) { it -> it.networkState }




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
