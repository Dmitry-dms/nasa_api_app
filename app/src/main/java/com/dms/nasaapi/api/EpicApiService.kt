package com.dms.nasaapi.api

import com.dms.nasaapi.model.epic.Epic
import com.dms.nasaapi.model.epic.EpicSearchResponse
import com.dms.nasaapi.model.image_library.NetworkState
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.coroutines.coroutineContext

fun getEpicByDate(
    service: EpicApiService,
    date: String,
    onSuccess: (items: List<Epic>)->Unit,
    networkState: (state: NetworkState) ->Unit
){
//    service.getEpicByDate(date).enqueue(object : Callback<List<Epic>>{
//        override fun onFailure(call: Call<List<Epic>>, t: Throwable) {
//            networkState(NetworkState.error("Error in response"))
//        }
//
//        override fun onResponse(
//            call: Call<List<Epic>>,
//            response: Response<List<Epic>>
//        ) {
//            networkState(NetworkState.LOADING)
//            if (response.isSuccessful){
//                val items = response.body() ?: emptyList()
//                onSuccess(items)
//                networkState(NetworkState.LOADED)
//            } else {
//                networkState(NetworkState.error("Unknown error"))
//            }
//        }
//
//
//    })
    val res = service.getEpicByDate(date).body()
    if (res==null) networkState(NetworkState.EMPTY)
    else res?.let {
        onSuccess(it)
        networkState(NetworkState.LOADED)
    }

}

interface EpicApiService {

    @GET("{date}?api_key=DEMO_KEY")
    fun getEpicByDate(@Path("date") date:String): Response<List<Epic>>

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/EPIC/api/natural/date/"

        fun create(): EpicApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EpicApiService::class.java)
        }
    }
}