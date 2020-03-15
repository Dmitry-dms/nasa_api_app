package com.dms.nasaapi.ui.epic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dms.nasaapi.data.epic.EpicRepository
import com.dms.nasaapi.ui.mrp.MrpViewModel

class EpicViewModelFactory(private val repository: EpicRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EpicViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}