package com.dms.nasaapi

import android.app.Application
import android.app.PictureInPictureParams
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.RetrofitClient
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.MarsRoverPhoto
import com.dms.nasaapi.model.PictureOfTheDay
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val appRepository: AppRepository = AppRepository(application)
    private var pod : LiveData<PictureOfTheDay>? = null
    private var marsPhoto : LiveData<List<MarsPhoto>>? = null

    private var api: NasaApiService
    private var api2: NasaApiService
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()



    fun updateApod(pod: PictureOfTheDay){
        appRepository.updateApod(pod)

    }
    fun getApod(): LiveData<PictureOfTheDay>? {
       // fetchData()
        pod = appRepository?.getApod()
        return pod
    }
    fun getMarsPhoto():LiveData<List<MarsPhoto>>? {
        marsPhoto=appRepository?.getAllPhoto()
        return marsPhoto

    }
    init {
        Log.d("TAG","init vm")
        val retrofit  = RetrofitClient.instance
        api = retrofit.create(NasaApiService::class.java)
        fetchData()

        val retrofitMars  =RetrofitClient.instanceMarsPhoto
        api2 = retrofitMars.create(NasaApiService::class.java)
        getMars()

    }
    private fun fetchData() {
        compositeDisposable.add(api.getPictureOfTheDay()
            .subscribeOn(Schedulers.io())
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
    private fun getMars(){
        compositeDisposable.add(api2.getMarsPhoto()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {success ->
                    appRepository.addMrp(success.photos)
                    Log.d("TAG","success retrofit mars")
                },
                {error -> error.printStackTrace()
                    Log.d("TAG","error retrofit mars") }
            )

        )
    }

}