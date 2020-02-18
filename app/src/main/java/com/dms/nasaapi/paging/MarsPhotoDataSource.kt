package com.dms.nasaapi.paging

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PageKeyedDataSource
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.RetrofitClient
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.MarsRoverPhoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsPhotoDataSource : PageKeyedDataSource<Int, MarsPhoto>() {
    //в самом начале вызывается
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MarsPhoto>
    ) {
        val service = RetrofitClient.buildMarsService(NasaApiService::class.java)
        val call = service?.getMarsPhoto(FIRST_PAGE)
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
                    responseItems?.let {
                        callback.onResult(responseItems, null, FIRST_PAGE + 1)
                    }

                }
            }

        })

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MarsPhoto>) {
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
                        callback.onResult(responseItems, key)
                    }

                }
            }

        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MarsPhoto>) {
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

                    val key = if (params.key > 1) params.key - 1 else 0


                    responseItems?.let {
                        callback.onResult(responseItems, key)
                    }

                }
            }

        })
    }

    companion object {
        const val FIRST_PAGE = 1
    }


}