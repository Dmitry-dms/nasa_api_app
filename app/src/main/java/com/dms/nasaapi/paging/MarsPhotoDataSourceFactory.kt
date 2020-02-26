package com.dms.nasaapi.paging

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.dms.nasaapi.model.MarsPhoto


class MarsPhotoDataSourceFactory(val application: Application) : DataSource.Factory<Int, MarsPhoto>() {

    val marsPhotoLiveDataSource = MutableLiveData<MarsPhotoDataSource>()

    override fun create(): DataSource<Int, MarsPhoto> {

        val repoDataSource = MarsPhotoDataSource(application)
        marsPhotoLiveDataSource.postValue(repoDataSource)
        return repoDataSource
    }
}