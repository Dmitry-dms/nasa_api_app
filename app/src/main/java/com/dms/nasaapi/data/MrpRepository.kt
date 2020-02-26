package com.dms.nasaapi.data

import android.util.Log
import androidx.paging.LivePagedListBuilder
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.db.marsRoverPhotos.MrpLocalCache
import com.dms.nasaapi.model.MrpSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class MrpRepository(
    private val service: NasaApiService,
    private val cache:MrpLocalCache
) {

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String): MrpSearchResult{
        Log.d("MrpRepository", "New query: $query")
        // Get data source factory from the local cache
        val dataSourceFactory = cache.getAllMarsPhoto()//query должна быть внутри параметров, но в данном случ. нет

        // Construct the boundary callback
        val boundaryCallback = MrpBoundaryCallback(query, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return MrpSearchResult(data, networkErrors)
    }
    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}