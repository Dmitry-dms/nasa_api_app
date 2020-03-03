package com.dms.nasaapi.ui.image_and_video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.dms.nasaapi.data.image_library.ImLDataSource
import com.dms.nasaapi.model.image_library.Item

class ImLDataSourceFactory(private val query: String) : DataSource.Factory<Int, Item>() {
    val queryLiveData = MutableLiveData<ImLDataSource>()


    override fun create(): DataSource<Int, Item> {
        val dataSource = ImLDataSource(query)
      //  queryLiveData.postValue(dataSource)
        Log.d("TAG2", "query = ${dataSource.em.value}")
        return dataSource
    }
}