package com.dms.nasaapi.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var myInstance: Retrofit? = null
    private var myInstanceMarsPhoto: Retrofit? = null

    var instance: Retrofit
    get()  {
        if(myInstance==null){
            myInstance = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.nasa.gov/planetary/")
                .build()

        }
        return myInstance!!
    }
        set(value) {}

    var instanceMarsPhoto: Retrofit
    get() {
        if(myInstanceMarsPhoto==null){
            myInstanceMarsPhoto = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/")
                .build()

        }
        return myInstanceMarsPhoto!!
    }
        set(value) {}
}