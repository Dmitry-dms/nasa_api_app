package com.dms.nasaapi

import android.app.Application
import android.app.PictureInPictureParams
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.RetrofitClient
import com.dms.nasaapi.model.PictureOfTheDay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val appRepository: AppRepository = AppRepository(application)
    private var pod : LiveData<PictureOfTheDay>? = null

    private lateinit var api: NasaApiService
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun updateApod(pod: PictureOfTheDay){
        appRepository.updateApod(pod)

    }
    fun getApod(): LiveData<PictureOfTheDay>? {
       // fetchData()
        pod = appRepository?.getApod()
        return pod
    }
    init {
        Log.d("TAG","init vm")
        val retrofit  = RetrofitClient.instance
        api = retrofit.create(NasaApiService::class.java)
        fetchData()
    }
    fun fetchData() {
        compositeDisposable.add(api.getPictureOfTheDay()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                    success -> appRepository.addApod(success)
                Log.d("TAG","success retrofit")
            }, {
                    error -> error.printStackTrace()
            Log.d("TAG","error retrofit")
            })
        )
    }

}