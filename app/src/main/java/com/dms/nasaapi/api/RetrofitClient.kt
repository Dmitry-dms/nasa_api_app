package com.dms.nasaapi.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var myInstance: Retrofit? = null
    private var myInstanceMarsPhoto: Retrofit? = null
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttp=OkHttpClient.Builder().addInterceptor(logger)



    fun <T> buildApodService(serviceType: Class<T>): T?{
        return if(myInstance==null){
            myInstance = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.nasa.gov/planetary/")
                .client(okHttp.build())
                .build()
            myInstance?.create(serviceType)
        } else {
            myInstance!!.create(serviceType)
        }


    }



    fun <T> buildMarsService(serviceType: Class<T>): T?{
        return if(myInstanceMarsPhoto==null){
            myInstanceMarsPhoto = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/")
                .client(okHttp.build())
                .build()
            myInstanceMarsPhoto?.create(serviceType)
        } else {
            myInstanceMarsPhoto!!.create(serviceType)
        }


    }
}