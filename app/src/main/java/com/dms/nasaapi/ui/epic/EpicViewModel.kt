package com.dms.nasaapi.ui.epic

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dms.nasaapi.data.epic.EpicRepository
import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.epic.EpicSearchResponse
import com.dms.nasaapi.model.image_library.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class EpicViewModel(private val repository: EpicRepository) : ViewModel() {
    private val queryLiveData = MutableLiveData<String>()

    private val result: LiveData<EpicSearchResponse> = Transformations.map(queryLiveData){
        repository.search(it)
    }

    val pagedList : LiveData<PagedList<Epic>> =
            Transformations.switchMap(result){
                it -> it.data
            }
    val state: LiveData<NetworkState> =
        Transformations.switchMap(result){
            it.networkErrors
        }
    fun search(queryDate: String){
        queryLiveData.postValue(queryDate)
    }
    fun lastQueryValue(): String? = queryLiveData.value


}
