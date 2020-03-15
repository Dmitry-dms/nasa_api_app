package com.dms.nasaapi.model.image_library

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>, //initial state
    val refreshState: LiveData<NetworkState>, // second state, after first data loaded
    val refresh: () -> Unit, // signal the data source to stop loading, and notify its callback
    val retry: () -> Unit,  // remake the call
    val clearCoroutineJobs: () -> Unit, // the way to stop jobs from running since no lifecycle provided
    val isEmpty: LiveData<NetworkState>
)

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    EMPTY,
    FULL
}

data class NetworkState private constructor(
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
        val EMPTY = NetworkState(Status.EMPTY)
        val FULL = NetworkState(Status.FULL)
    }
}