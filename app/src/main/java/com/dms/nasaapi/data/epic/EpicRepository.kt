package com.dms.nasaapi.data.epic

import androidx.paging.LivePagedListBuilder
import com.dms.nasaapi.api.EpicApiService
import com.dms.nasaapi.data.BaseRepository
import com.dms.nasaapi.db.epic.EpicDAO
import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.Listing
import com.dms.nasaapi.model.NetworkState

class EpicRepository(private val service: EpicApiService,private val epicDAO: EpicDAO) : BaseRepository() {

    override fun search(date: String): Listing<Epic> {
        val factory = epicDAO.getEpic(date)
        val callback = EpicBoundaryCallback(date,service,epicDAO)

        val state = callback.networkState
        val data = LivePagedListBuilder(factory,13)
            .setBoundaryCallback(callback)
            .build()
        return Listing(
            pagedList = data,
            networkState = state,
            retry = { callback.retryAllFailed() },
            clearCoroutineJobs = { callback.clearCoroutineJob() }
        )

    }
}