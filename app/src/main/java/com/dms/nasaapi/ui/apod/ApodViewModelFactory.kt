package com.dms.nasaapi.ui.apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dms.nasaapi.data.apod.ApodRepository
import com.dms.nasaapi.data.image_library.ImLRepository
import com.dms.nasaapi.ui.image_and_video.ImageLibraryViewModel

class ApodViewModelFactory(private val repository: ApodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}