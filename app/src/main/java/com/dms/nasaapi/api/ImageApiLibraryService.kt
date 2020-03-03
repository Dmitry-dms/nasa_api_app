package com.dms.nasaapi.api

import android.util.Log
import com.dms.nasaapi.model.image_library.Collection
import com.dms.nasaapi.model.image_library.ImageLibrarySearchResponse
import com.dms.nasaapi.model.image_library.Item
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
private const val TAG = "searchImageLibrary"
fun searchImageLibrary(
    service: ImageApiLibraryService,
    query: String,
    page: Int,
    onSuccess: (items: List<Item>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "query: $query, page: $page")
    service.getImageBySearch(query,page).enqueue(object : Callback<ImageLibrarySearchResponse>{
        override fun onFailure(call: Call<ImageLibrarySearchResponse>, t: Throwable) {
            Log.d(TAG, "fail to get data")
            onError(t.message ?: "unknown error")
        }

        override fun onResponse(
            call: Call<ImageLibrarySearchResponse>,
            response: Response<ImageLibrarySearchResponse>
        ) {
            Log.d(TAG, "got a response $response")
            if(response.isSuccessful){
                val items = response.body()?.collection?.items ?: emptyList()
                onSuccess(items)
            } else {
                onError(response.errorBody()?.string() ?: "Unknown error")
            }

        }

    })

}

interface ImageApiLibraryService {
    @GET("search?")
    fun getImageBySearch(
        @Query("q") search: String,
        @Query("page") page: Int
    ): Call<ImageLibrarySearchResponse>

    companion object {
        private const val BASE_URL = "https://images-api.nasa.gov/"

        fun create(): ImageApiLibraryService {
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
                .create(ImageApiLibraryService::class.java)
        }
    }
}