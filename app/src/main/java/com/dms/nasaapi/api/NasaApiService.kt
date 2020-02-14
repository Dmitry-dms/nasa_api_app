package com.dms.nasaapi.api

import com.dms.nasaapi.model.PictureOfTheDay
import io.reactivex.Observable
import retrofit2.http.GET

interface NasaApiService {

    @GET("apod?api_key=EitOWCCokyblxbk3Gs5JwiQyiZH9qIpBpDB5G2je")
    fun getPictureOfTheDay(): Observable<PictureOfTheDay>


}