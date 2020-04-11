package com.dms.nasaapi.ui.apod

import androidx.lifecycle.ViewModel
import com.dms.nasaapi.data.apod.ApodRepository

class ApodViewModel(private val repository: ApodRepository): ViewModel() {

    val result = repository.search()
    val networkState = repository.networkState
    init {
        //repository.update()
    }
    fun clearCoroutineJob(){
        repository.clearCoroutineJob()
    }


}