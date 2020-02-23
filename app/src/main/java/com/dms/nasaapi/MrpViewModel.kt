package com.dms.nasaapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.paging.MarsPhotoDataSource
import com.dms.nasaapi.paging.MarsPhotoDataSourceFactory
import com.dms.nasaapi.room.marsRoverPhotos.MrpDAO
import com.dms.nasaapi.room.marsRoverPhotos.MrpDatabase

class MrpViewModel: ViewModel() {

    var marsPhotoPagedList: LiveData<PagedList<MarsPhoto>>


  //  private var liveDataSource: LiveData<MarsPhotoDataSource>

    init {

        val itemDataSourceFactory= MarsPhotoDataSourceFactory()
       // liveDataSource = itemDataSourceFactory.marsPhotoLiveDataSource

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(25)
            .build()

        marsPhotoPagedList = LivePagedListBuilder(itemDataSourceFactory,config).build()
    }
}