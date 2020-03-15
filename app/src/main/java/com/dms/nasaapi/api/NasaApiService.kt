package com.dms.nasaapi.api

import android.util.Log
import com.dms.nasaapi.model.mrp.MarsPhoto
import com.dms.nasaapi.model.PictureOfTheDay
import com.dms.nasaapi.model.epic.EpicSearchResponse
import com.dms.nasaapi.model.image_library.ImageLibrarySearchResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val TAG = "NasaApiService"


/**
 * Search repos based on a query.
 * Trigger a request to the Github searchRepo API with the following params:
 * @param query searchRepo keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Github API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of repos received
 * @param onError function that defines how to handle request failure
 */
fun searchMarsPhotos(
    service: NasaApiService,
    query: String,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (photos: List<MarsPhoto>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage")

    service.getMarsPhoto(page, itemsPerPage).enqueue(object : Callback<MrpSearchResponse>{
        override fun onFailure(call: Call<MrpSearchResponse>, t: Throwable) {
            Log.d(TAG, "fail to get data")
            onError(t.message ?: "unknown error")
        }

        override fun onResponse(call: Call<MrpSearchResponse>, response: Response<MrpSearchResponse>) {
            Log.d(TAG, "got a response $response")
            if (response.isSuccessful) {
                val repos = response.body()?.photos ?: emptyList()
                onSuccess(repos)
            } else {
                onError(response.errorBody()?.string() ?: "Unknown error")
            }
        }
    })
}


interface NasaApiService {

    @GET("apod?api_key=EitOWCCokyblxbk3Gs5JwiQyiZH9qIpBpDB5G2je")
    fun getPictureOfTheDay(): Observable<PictureOfTheDay>

    @GET("photos?sol=1000&api_key=DEMO_KEY")
    fun getMarsPhoto(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Call<MrpSearchResponse>


    companion object {
        private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/"

        fun create(): NasaApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client =OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NasaApiService::class.java)
        }
    }

}