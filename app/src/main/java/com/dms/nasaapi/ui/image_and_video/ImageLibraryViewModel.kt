package com.dms.nasaapi.ui.image_and_video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dms.nasaapi.data.image_library.ImLDataSource
import com.dms.nasaapi.data.image_library.ImLRepository
import com.dms.nasaapi.model.image_library.ImLSearchResult
import com.dms.nasaapi.model.image_library.Item

class ImageLibraryViewModel(private val repository: ImLRepository) : ViewModel() {
//     var itemsPagedList : LiveData<PagedList<Item>>
//     var liveDataSource: LiveData<ImLDataSource>


    //
    private val queryLiveData = MutableLiveData<String>()
    private val queryResult: LiveData<ImLSearchResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }
    val items: LiveData<PagedList<Item>> = Transformations.switchMap(queryResult) { it -> it.data }
    val networkErrors: LiveData<String> =
        Transformations.switchMap(queryResult) { it -> it.networkErrors }
    val isEmpty: LiveData<Boolean> = Transformations.switchMap(queryResult){it->it.isEmpty}

    //    init {
//        val factory = ImLDataSourceFactory("mars")
//        liveDataSource = factory.queryLiveData
//
//        val config  = PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setPageSize(100)
//            .build()
//        itemsPagedList = LivePagedListBuilder(factory,config).build()
//    }
    fun searchImages(query: String) {
        queryLiveData.postValue(query)
    }

    fun lastQueryValue(): String? = queryLiveData.value
}
