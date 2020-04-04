package com.dms.nasaapi.ui.apod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dms.nasaapi.data.apod.ApodRepository
import com.dms.nasaapi.model.PictureOfTheDay

class ApodViewModel(private val repository: ApodRepository): ViewModel() {
    private val data = MutableLiveData<Int>()

    val result = repository.search()

}