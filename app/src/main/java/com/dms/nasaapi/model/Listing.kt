package com.dms.nasaapi.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val retry: () -> Unit,
    val clearCoroutineJobs: () -> Unit
)

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    EMPTY
}

data class NetworkState constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val LOADED =
            NetworkState(Status.SUCCESS)
        val LOADING =
            NetworkState(Status.RUNNING)

        fun error(msg: String?) = NetworkState(
            Status.FAILED,
            msg
        )
        val EMPTY =
            NetworkState(Status.EMPTY)
    }
}