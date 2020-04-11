package com.dms.nasaapi.ui.mrp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dms.nasaapi.data.mrp.MrpRepository
import com.dms.nasaapi.model.Listing
import com.dms.nasaapi.model.NetworkState
import com.dms.nasaapi.model.mrp.MarsPhoto


class MrpViewModel(private val repository: MrpRepository) : ViewModel() {


    private val queryLiveData = MutableLiveData<String>()
    private val result: LiveData<Listing<MarsPhoto>> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val marsPhotoPagedList: LiveData<PagedList<MarsPhoto>> =
        Transformations.switchMap(result) { it.pagedList }

    val networkErrors: LiveData<NetworkState> = Transformations.switchMap(result) {
        it.networkState
    }

    fun searchMrp(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun lastQueryValue(): String? = queryLiveData.value

    override fun onCleared() {
        super.onCleared()
        result.value?.clearCoroutineJobs?.invoke()
    }

    fun retry() {
        result.value?.retry?.invoke()
    }
}