package com.dms.nasaapi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dms.nasaapi.data.MrpRepository


class MrpViewModelFactory(private val repository: MrpRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MrpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MrpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}