package com.dms.nasaapi.data.epic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dms.nasaapi.api.EpicApiService
import com.dms.nasaapi.db.epic.EpicDAO
import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.image_library.NetworkState
import kotlinx.coroutines.*
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

    private var retry: (() -> Any)? = null
    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }
    val networkErrors: LiveData<NetworkState>
        get() = _networkErrors
    private var isRequestInProgress = false

    private fun requestAndSaveData(date: String) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        coroutineScope.launch {

            try {
                val response= service.getEpicByDate(date)
                val reg = Regex("(-)|(:)|( )")
                if (response[0].identifier!=null){
                    Log.d("EPIC","start")
                    response.forEach{
                        Log.d("EPIC","${Thread().name}")
                        val (year,month,day) = it.date.split(reg)
                        it.date=it.date.split(" ")[0]
                        it.year = year
                        it.month = month
                        it.day = day
                    }
                    dao.add(response)
                    _networkErrors.postValue(NetworkState.LOADED)
                    isRequestInProgress = false
                } else {
                    _networkErrors.postValue(NetworkState.EMPTY)
                    isRequestInProgress = false
                }
            } catch (e: Exception) {
                _networkErrors.postValue(NetworkState.error("smth went wrong!"))
                _networkErrors.postValue(NetworkState.error(e.message))
                e.printStackTrace()
                isRequestInProgress = false
            } finally {

            }
        }

    }

    override fun onZeroItemsLoaded() {
        requestAndSaveData(date)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Epic) {
        //requestAndSaveData("2020-03-01")
        val date = Date()
        val (year,month,day) = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date).split("-")
        Log.d("EPIC", "$year")
    }

    fun clearCoroutineJob() {
        coroutineScope.cancel()
    }
}