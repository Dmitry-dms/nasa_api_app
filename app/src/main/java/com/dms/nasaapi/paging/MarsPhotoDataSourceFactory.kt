package com.dms.nasaapi.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.dms.nasaapi.model.MarsPhoto


class MarsPhotoDataSourceFactory : DataSource.Factory<Int, MarsPhoto>() {

    val marsPhotoLiveDataSource = MutableLiveData<MarsPhotoDataSource>()

    override fun create(): DataSource<Int, MarsPhoto> {

        val repoDataSource = MarsPhotoDataSource()
        marsPhotoLiveDataSource.postValue(repoDataSource)
        return repoDataSource
    }
}