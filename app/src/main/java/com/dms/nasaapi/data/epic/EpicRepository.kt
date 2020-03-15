package com.dms.nasaapi.data.epic

import androidx.paging.LivePagedListBuilder
import com.dms.nasaapi.api.EpicApiService
import com.dms.nasaapi.db.epic.EpicDAO
import com.dms.nasaapi.model.epic.EpicSearchResponse

class EpicRepository(private val service: EpicApiService,private val epicDAO: EpicDAO) {

    fun search(date: String): EpicSearchResponse{
        val factory = epicDAO.getEpic()

        val callback = EpicBoundaryCallback(date,service,epicDAO)

        val state = callback.networkErrors
        val data = LivePagedListBuilder(factory,5)
            .setBoundaryCallback(callback)
            .build()
        return EpicSearchResponse(data,state)

    }
}