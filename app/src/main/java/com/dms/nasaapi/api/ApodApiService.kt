package com.dms.nasaapi.api

import com.dms.nasaapi.ext.DEMO_KEY
import com.dms.nasaapi.model.apod.PictureOfTheDay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET

interface ApodApiService {

    @GET("apod?api_key=$DEMO_KEY")
    suspend fun getPictureOfTheDay(): PictureOfTheDay

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/planetary/"
        fun create(): ApodApiService {
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
                .create(ApodApiService::class.java)
        }
    }
}