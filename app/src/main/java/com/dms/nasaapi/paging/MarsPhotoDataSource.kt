package com.dms.nasaapi.paging

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PageKeyedDataSource
import androidx.room.Room
import com.dms.nasaapi.AppRepository
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.RetrofitClient
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.MarsRoverPhoto
import com.dms.nasaapi.room.marsRoverPhotos.MrpDAO
import com.dms.nasaapi.room.marsRoverPhotos.MrpDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsPhotoDataSource(application: Application) : PageKeyedDataSource<Int, MarsPhoto>() {
    var repo = AppRepository(application)



    //в самом начале вызывается
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MarsPhoto>) {

        val list = repo.getMarsBySize(0,params.requestedLoadSize)

        if(list.size<25){
            val service = RetrofitClient.buildMarsService(NasaApiService::class.java)
            val call = service?.getMarsPhoto(FIRST_PAGE)

            Log.d("TAG", "загрузка из сети")
            call?.enqueue(object : Callback<MarsRoverPhoto> {
            override fun onFailure(call: Call<MarsRoverPhoto>, t: Throwable) {
                Log.d("TAG", "mars error")

            }

            override fun onResponse(call: Call<MarsRoverPhoto>, response: Response<MarsRoverPhoto>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.photos
                    responseItems?.let {
                        repo.addMrp(it)
                        callback.onResult(responseItems, null, FIRST_PAGE + 1)
                    }

                }
            }

        })
        } else {

            callback.onResult(list,null,1)
            Log.d("TAG", "загрузка из бд ${params.requestedLoadSize}")

        }



    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MarsPhoto>) {


        val list = repo.getMarsBySize(params.key*25,params.requestedLoadSize)

        Log.d("TAG", "load after ${params.key}")
        if (list.size<25){
            val service = RetrofitClient.buildMarsService(NasaApiService::class.java)
            val call = service?.getMarsPhoto(params.key)

            call?.enqueue(object : Callback<MarsRoverPhoto> {
            override fun onFailure(call: Call<MarsRoverPhoto>, t: Throwable) {
                Log.d("TAG", "mars error")
            }

            override fun onResponse(
                call: Call<MarsRoverPhoto>,
                response: Response<MarsRoverPhoto>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.photos

                    val key = if (25 > params.key) params.key + 1 else 0


                    responseItems?.let {
                        repo.addMrp(it)
                        callback.onResult(responseItems, key)
                    }

                }
            }

        })
            Log.d("TAG", "загрузка из сети")
        } else{
            val nextKey = list.size-params.key
            callback.onResult(list,nextKey)
            Log.d("TAG", "загрузка из бд ${params.requestedLoadSize}")
        }
//
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MarsPhoto>) {
        val service = RetrofitClient.buildMarsService(NasaApiService::class.java)
        val call = service?.getMarsPhoto(params.key)

        val list = repo.getMarsBySize(params.key*25-25,params.requestedLoadSize)
        val nextKey = params.key-list.size
        callback.onResult(list,nextKey)
        Log.d("TAG", "load before")
//        call?.enqueue(object : Callback<MarsRoverPhoto> {
//            override fun onFailure(call: Call<MarsRoverPhoto>, t: Throwable) {
//                Log.d("TAG", "mars error")
//            }
//
//            override fun onResponse(call: Call<MarsRoverPhoto>, response: Response<MarsRoverPhoto>) {
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()!!
//                    val responseItems = apiResponse.photos
//
//                    val key = if (params.key > 1) params.key - 1 else 0
//
//
//                    responseItems?.let {
//                        callback.onResult(responseItems, key)
//                    }
//
//                }
//            }
//
//        })
    }

    companion object {
        const val FIRST_PAGE = 1
    }


}