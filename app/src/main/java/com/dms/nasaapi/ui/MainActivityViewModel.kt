package com.dms.nasaapi.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dms.nasaapi.AppRepository
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.RetrofitClient
import com.dms.nasaapi.model.mrp.MarsPhoto

import com.dms.nasaapi.model.PictureOfTheDay
import com.dms.nasaapi.model.image_library.ImageLibrarySearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val appRepository: AppRepository =
        AppRepository(application)
    private var pod : LiveData<PictureOfTheDay>? = null


    private var api: NasaApiService?

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()



    fun updateApod(pod: PictureOfTheDay){
        appRepository.updateApod(pod)

    }
    fun getApod(): LiveData<PictureOfTheDay>? {
        pod = appRepository?.getApod()
        return pod
    }

    init {
        Log.d("TAG","init vm")

        api = RetrofitClient.buildApodService(NasaApiService::class.java)
        fetchData()

    }
    private fun fetchData() {
        compositeDisposable.add(api?.getPictureOfTheDay()
            !!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
       //     .repeatWhen{it.delay(12, TimeUnit.SECONDS)}// интервал для запросов
            .subscribe({
                    success ->
                    appRepository.addApod(success)
                    Log.d("TAG","success retrofit")
            }, {
                    error -> error.printStackTrace()
                    Log.d("TAG","error retrofit")
            })


        )
    }

}