package com.dms.nasaapi.data.mrp

import androidx.paging.LivePagedListBuilder
import com.dms.nasaapi.api.MrpApiService
import com.dms.nasaapi.data.BaseRepository
import com.dms.nasaapi.db.marsRoverPhotos.MrpDAO

import com.dms.nasaapi.model.Listing
import com.dms.nasaapi.model.NetworkState
import com.dms.nasaapi.model.mrp.MarsPhoto

/**
 * Repository class that works with local and remote data sources.
 */
class MrpRepository(private val service: MrpApiService, private val dao: MrpDAO) :
    BaseRepository() {

    override fun search(query: String): Listing<MarsPhoto> {
        val dataSourceFactory = dao.getAllMarsPhotos()
        val boundaryCallback =
            MrpBoundaryCallback(query, service, dao)

        val networkState = boundaryCallback.networkState
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()
        return Listing(
            pagedList = data,
            networkState = networkState,
            retry = { boundaryCallback.retryAllFailed() },
            clearCoroutineJobs = { boundaryCallback.clearCoroutineJob() }
        )
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}