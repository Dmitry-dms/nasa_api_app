package com.dms.nasaapi.data.image_library

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.data.BaseRepository
import com.dms.nasaapi.model.image_library.Item
import com.dms.nasaapi.model.Listing

import com.dms.nasaapi.ui.image_and_video.ImLDataSourceFactory

class ImLRepository(private val service: ImageApiLibraryService) : BaseRepository(){


    override fun search(query: String): Listing<Item> {
        val factory = ImLDataSourceFactory(query, service)
        val config = PagedList.Config.Builder()
            .setPageSize(100)
            .setEnablePlaceholders(false)
            .build()

        val pagedListBuilder = LivePagedListBuilder(factory, config)
            .build()
        return Listing(
            pagedList = pagedListBuilder,
            networkState = Transformations.switchMap(factory.mutableDataSource) { it.networkState },
            retry = {
                factory.mutableDataSource.value?.retryAllFailed()
            },
            clearCoroutineJobs = {
                factory.mutableDataSource.value?.clearCoroutineJobs()
            }
        )
    }
}