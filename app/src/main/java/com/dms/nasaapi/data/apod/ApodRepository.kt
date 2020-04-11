package com.dms.nasaapi.data.apod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dms.nasaapi.api.ApodApiService
import com.dms.nasaapi.data.BaseRepository
import com.dms.nasaapi.db.apod.ApodDAO
import com.dms.nasaapi.model.Listing
import com.dms.nasaapi.model.NetworkState
import com.dms.nasaapi.model.apod.PictureOfTheDay
import kotlinx.coroutines.*
import java.lang.Exception

class ApodRepository(private val service: ApodApiService, private val dao: ApodDAO) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    fun search(): LiveData<PictureOfTheDay> {
        val result = dao.getApod()
        if (result.value == null) update()
        return result
    }
    fun clearCoroutineJob(){
        job.cancel()
    }
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private fun update() {
        coroutineScope.launch {
            try {
                _networkState.postValue(NetworkState.LOADING)
                val response = service.getPictureOfTheDay()

                if (response.explanation.isNotEmpty()) {
                    dao.add(response)
                    _networkState.postValue(NetworkState.LOADED)
                } else {
                    _networkState.postValue(NetworkState.EMPTY)
                }
            }catch (e: Exception){
                _networkState.postValue(NetworkState.error("Network error"))
                e.printStackTrace()
            }
        }
    }


}