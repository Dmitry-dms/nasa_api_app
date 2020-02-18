package com.dms.nasaapi.api

import androidx.lifecycle.LiveData
import com.dms.nasaapi.model.MarsPhoto
import com.dms.nasaapi.model.MarsRoverPhoto
import com.dms.nasaapi.model.PictureOfTheDay
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {

    @GET("apod?api_key=EitOWCCokyblxbk3Gs5JwiQyiZH9qIpBpDB5G2je")
    fun getPictureOfTheDay(): Observable<PictureOfTheDay>

//    @GET("photos?sol=1000&api_key=DEMO_KEY")
//    fun getMarsPhoto():Observable<MarsRoverPhoto>

    @GET("photos?sol=1000&api_key=DEMO_KEY")
    fun getMarsPhoto(
        @Query("page") page : Int
    ):Call<MarsRoverPhoto>


}