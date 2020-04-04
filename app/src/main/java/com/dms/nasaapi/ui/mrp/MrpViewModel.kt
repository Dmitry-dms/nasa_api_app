package com.dms.nasaapi.ui.mrp

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dms.nasaapi.data.mrp.MrpRepository
import com.dms.nasaapi.model.image_library.Listing
import com.dms.nasaapi.model.image_library.NetworkState
import com.dms.nasaapi.model.mrp.MarsPhoto


/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [GithubRepository] to get the data.
 */
class MrpViewModel(private val repository: MrpRepository) : ViewModel() {


    private val queryLiveData = MutableLiveData<String>()
    private val result: LiveData<Listing<MarsPhoto>> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val marsPhotoPagedList: LiveData<PagedList<MarsPhoto>> =
        Transformations.switchMap(result) { it -> it.pagedList }

    val networkErrors: LiveData<NetworkState> = Transformations.switchMap(result) { it ->
        it.networkState
    }

    /**
     * Search a repository based on a query string.
     */
    fun searchMrp(queryString: String) {
        queryLiveData.postValue(queryString)
    }


    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value

    override fun onCleared() {
        super.onCleared()
        result.value?.clearCoroutineJobs?.invoke()
    }
    fun retry(){
        result.value?.retry?.invoke()
    }
}