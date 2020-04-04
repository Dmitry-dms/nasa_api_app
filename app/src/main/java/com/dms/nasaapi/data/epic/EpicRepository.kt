package com.dms.nasaapi.data.epic

import androidx.paging.LivePagedListBuilder
import com.dms.nasaapi.api.EpicApiService
import com.dms.nasaapi.db.epic.EpicDAO
import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.epic.EpicSearchResponse
import com.dms.nasaapi.model.image_library.Listing

class EpicRepository(private val service: EpicApiService,private val epicDAO: EpicDAO) {

    fun search(date: String): Listing<Epic>{
        val factory = epicDAO.getEpic(date)

        val callback = EpicBoundaryCallback(date,service,epicDAO)

        val state = callback.networkErrors
        val data = LivePagedListBuilder(factory,13)
            .setBoundaryCallback(callback)
            .build()
        return Listing(
            data,state,
            {
                callback.retryAllFailed()
            },
            {

            },
            {
                callback.clearCoroutineJob()
            }
        )

    }
}