package com.dms.nasaapi.ui.image_and_video

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.data.image_library.ImLDataSource
import com.dms.nasaapi.model.image_library.Item

class ImLDataSourceFactory(private val query: String,private val api: ImageApiLibraryService) :
    DataSource.Factory<Int, Item>() {

    val mutableDataSource: MutableLiveData<ImLDataSource> = MutableLiveData()
    private lateinit var mImLDataSource: ImLDataSource


    override fun create(): DataSource<Int, Item> {

        mImLDataSource = ImLDataSource(query,api)
        mutableDataSource.postValue(mImLDataSource)
        Log.d("TAG2", "state = ${mImLDataSource.networkState.value?.msg}")
        return mImLDataSource
    }
}