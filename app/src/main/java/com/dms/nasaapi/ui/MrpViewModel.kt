package com.dms.nasaapi.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dms.nasaapi.data.MrpRepository
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.MrpSearchResult


/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [GithubRepository] to get the data.
 */
class MrpViewModel(private val repository: MrpRepository) : ViewModel() {


    private val queryLiveData = MutableLiveData<String>()
    private val mrpResult: LiveData<MrpSearchResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val marsPhotoPagedList: LiveData<PagedList<MarsPhoto>> =
        Transformations.switchMap(mrpResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(mrpResult) { it ->
        it.networkErrors
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
}