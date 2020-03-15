package com.dms.nasaapi.data.epic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dms.nasaapi.api.EpicApiService
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.getEpicByDate
import com.dms.nasaapi.db.epic.EpicDAO
import com.dms.nasaapi.db.marsRoverPhotos.MrpLocalCache
import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.image_library.NetworkState
import com.dms.nasaapi.model.mrp.MarsPhoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EpicBoundaryCallback(
    private val date: String,
    private val service: EpicApiService,
    private val dao: EpicDAO
) : PagedList.BoundaryCallback<Epic>() {
    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob) //coroutine scope

    private val _networkErrors = MutableLiveData<NetworkState>()
    val networkErrors: LiveData<NetworkState>
        get() = _networkErrors
    private var isRequestInProgress = false

    private fun requestAndSaveData(date: String){
        if (isRequestInProgress) return
        coroutineScope.launch {
            isRequestInProgress = true

            getEpicByDate(service, date,
                { items ->
                    items.forEach {
                        val reg = Regex("(-)|(:)|( )")
                        val text = it.date.split(reg)
                        it.year = text[0]
                        it.month = text[1]
                        it.day = text[2]
                    }
                    dao.add(items)
                    isRequestInProgress = false
                }, {
                    _networkErrors.postValue(it)
                    isRequestInProgress = false
                }
            )
        }
    }

    override fun onZeroItemsLoaded() {
        requestAndSaveData(date)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Epic) {
        //requestAndSaveData("2020-03-01")
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val text = dateFormat.format(date)
        Log.d("EPIC","$text")
    }
}