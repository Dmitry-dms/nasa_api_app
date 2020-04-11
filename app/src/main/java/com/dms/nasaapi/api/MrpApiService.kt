package com.dms.nasaapi.api


import com.dms.nasaapi.ext.DEMO_KEY
import com.dms.nasaapi.model.mrp.MrpSearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MrpApiService {
    @GET("photos?sol=1000&api_key=$DEMO_KEY")
    suspend fun getMarsPhoto(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): MrpSearchResponse


    companion object {
        private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/"

        fun create(): MrpApiService {
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
                .create(MrpApiService::class.java)
        }
    }
}