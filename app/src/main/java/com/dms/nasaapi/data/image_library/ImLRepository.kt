package com.dms.nasaapi.data.image_library

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.data.mrp.MrpBoundaryCallback
import com.dms.nasaapi.data.mrp.MrpRepository
import com.dms.nasaapi.model.image_library.ImLSearchResult
import com.dms.nasaapi.model.mrp.MrpSearchResult
import com.dms.nasaapi.ui.image_and_video.ImLDataSourceFactory

class ImLRepository(private val service: ImageApiLibraryService) {


    fun search(query: String) : ImLSearchResult{
        val factory = ImLDataSourceFactory(query)
        val boundaryCallback = ImLBoundaryCallback(query, service)
        val networkErrors =boundaryCallback.networkErrors
        val empty = boundaryCallback.isEmpty
       // Log.d("TAG2", "query = ${factory.queryLiveData.value}")

        val data =LivePagedListBuilder(factory,100)
            //.setBoundaryCallback(boundaryCallback)
            .build()
        return ImLSearchResult(data, networkErrors,empty)
    }
}