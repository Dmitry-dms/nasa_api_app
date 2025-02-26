package com.dms.nasaapi.api

import com.dms.nasaapi.ext.DEMO_KEY
import com.dms.nasaapi.model.epic.Epic
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface EpicApiService {

    @GET("{date}?api_key=$DEMO_KEY")
    suspend fun getEpicByDate(@Path("date") date: String): List<Epic>

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
