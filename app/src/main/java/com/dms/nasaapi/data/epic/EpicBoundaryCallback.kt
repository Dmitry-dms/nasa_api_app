package com.dms.nasaapi.data.epic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dms.nasaapi.api.EpicApiService
import com.dms.nasaapi.db.epic.EpicDAO
import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.NetworkState
import kotlinx.coroutines.*

class EpicBoundaryCallback(
    private val date: String,
    private val service: EpicApiService,
    private val dao: EpicDAO
) : PagedList.BoundaryCallback<Epic>() {
    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob) //coroutine scope

    val _networkState = MutableLiveData<NetworkState>()

    private var retry: (() -> Any)? = null
    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    val networkState: LiveData<NetworkState>
        get() = _networkState


    private fun requestAndSaveData(date: String) {
        coroutineScope.launch {
            try {
                _networkState.postValue(NetworkState.LOADING)
                val response = service.getEpicByDate(date)
                val reg = Regex("(-)|(:)|( )")
                if (response.isNotEmpty()) {

                    response.forEach {
                        Log.d("EPIC", Thread().name)
                        val (year, month, day) = it.date.split(reg)
                        it.date = it.date.split(" ")[0]
                        it.year = year
                        it.month = month
                        it.day = day
                    }
                    dao.add(response)
                    _networkState.postValue(NetworkState.LOADED)
                } else {
                    _networkState.postValue(NetworkState.EMPTY)

                }
            } catch (e: Exception) {
                _networkState.postValue(NetworkState.error("Network error"))
                e.printStackTrace()
                retry = {
                    requestAndSaveData(date)
                }
            }

        }
    }

        override fun onZeroItemsLoaded() {
            requestAndSaveData(date)
        }

        override fun onItemAtEndLoaded(itemAtEnd: Epic) {
        }

        fun clearCoroutineJob() {
            completableJob.cancel()
        }
    }