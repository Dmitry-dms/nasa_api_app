package com.dms.nasaapi.api

import com.dms.nasaapi.model.image_library.ImageLibrarySearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ImageApiLibraryService {

    @GET("search?")
    suspend fun getImageBySearch(
        @Query("q") search: String,
        @Query("page") page: Int
    ): ImageLibrarySearchResponse

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