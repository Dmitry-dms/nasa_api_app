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
    private var marsPhoto : LiveData<List<MarsPhoto>>? = null

    private var api: NasaApiService?
  //  private var api2: NasaApiService?
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var retrofitImage: NasaApiService? = null


    fun updateApod(pod: PictureOfTheDay){
        appRepository.updateApod(pod)

    }
    fun getApod(): LiveData<PictureOfTheDay>? {
       // fetchData()
        pod = appRepository?.getApod()
        return pod
    }
//    fun getMarsPhoto():LiveData<List<MarsPhoto>>? {
//        marsPhoto=appRepository?.getAllPhoto()
//        return marsPhoto
//
//    }
    init {
        Log.d("TAG","init vm")
       // val retrofit  = RetrofitClient.instance
        api = RetrofitClient.buildApodService(NasaApiService::class.java)
            //retrofit.create(NasaApiService::class.java)
        fetchData()

     //   retrofitImage  =RetrofitClient.buildLibService(NasaApiService::class.java)

       // getImage("mars","1")

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
//    private fun getImage(q:String,page:String) {
//        val call = retrofitImage?.getImageBySearch(q, page)
//        call?.enqueue(object : Callback<ImageLibrarySearchResponse> {
//            override fun onFailure(call: Call<ImageLibrarySearchResponse>, t: Throwable) {
//                Log.d("TAG","ошибка либрари $t")
//            }
//
//            override fun onResponse(
//                call: Call<ImageLibrarySearchResponse>,
//                response: Response<ImageLibrarySearchResponse>
//            ) {
//                if (response.isSuccessful){
//                    val apiResponse = response.body()!!
//                    val responseItems = apiResponse.collection.items[1].links[0].href
//
//                   // val size = responseItems?.let { responseItems.size.toString() }
//                   // Log.d("TAG","kol-vo v massive $size")
//                    Log.d("TAG","res body ${responseItems}")
//                }
//            }
//
//
//        })
//    }

//        compositeDisposable.add(api2.getMarsPhoto()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {success ->
//                    appRepository.addMrp(success.photos)
//                    Log.d("TAG","success retrofit mars")
//                },
//                {error -> error.printStackTrace()
//                    Log.d("TAG","error retrofit mars") }
//            )
//
//        )
   // }

}