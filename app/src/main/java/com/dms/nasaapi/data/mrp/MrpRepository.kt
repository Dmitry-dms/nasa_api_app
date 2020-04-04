package com.dms.nasaapi.data.mrp

import android.util.Log
import androidx.paging.LivePagedListBuilder
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.db.marsRoverPhotos.MrpDAO

import com.dms.nasaapi.model.image_library.Listing
import com.dms.nasaapi.model.mrp.MarsPhoto
import com.dms.nasaapi.model.mrp.MrpSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class MrpRepository(
    private val service: NasaApiService,
    private val dao: MrpDAO
) {


    fun search(query: String): Listing<MarsPhoto> {

        val dataSourceFactory = dao.getAllMarsPhotos()


        val boundaryCallback =
            MrpBoundaryCallback(query, service, dao)
        val networkState = boundaryCallback.networkErrors


        val data = LivePagedListBuilder(dataSourceFactory,
            DATABASE_PAGE_SIZE
        )
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Listing(
            data,networkState,{
                boundaryCallback.retryAllFailed()
            },{},{
                boundaryCallback.clearCoroutineJob()
            }
        )
    }
    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}