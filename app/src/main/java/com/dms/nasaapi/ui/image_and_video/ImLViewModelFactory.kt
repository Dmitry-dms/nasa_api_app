package com.dms.nasaapi.ui.image_and_video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dms.nasaapi.data.image_library.ImLRepository
import com.dms.nasaapi.data.mrp.MrpRepository
import com.dms.nasaapi.ui.mrp.MrpViewModel

class ImLViewModelFactory(private val repository: ImLRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageLibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageLibraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}