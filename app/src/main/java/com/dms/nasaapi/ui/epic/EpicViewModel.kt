package com.dms.nasaapi.ui.epic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dms.nasaapi.data.epic.EpicRepository
import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.image_library.Listing
import com.dms.nasaapi.model.image_library.NetworkState

class EpicViewModel(private val repository: EpicRepository) : ViewModel() {
    private val queryLiveData = MutableLiveData<String>()

    private val result: LiveData<Listing<Epic>> = Transformations.map(queryLiveData){
        repository.search(it)
    }

    val pagedList : LiveData<PagedList<Epic>> =
            Transformations.switchMap(result){
                it -> it.pagedList
            }
    val state: LiveData<NetworkState> =
        Transformations.switchMap(result){
            it.networkState
        }
    fun search(queryDate: String){
        queryLiveData.postValue(queryDate)
    }
    fun lastQueryValue(): String? = queryLiveData.value
    override fun onCleared() {
        super.onCleared()
        result.value?.clearCoroutineJobs?.invoke()
    }
    fun retry(){
        result.value?.retry?.invoke()
    }
}
