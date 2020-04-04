package com.dms.nasaapi.data

import android.util.Log
import com.dms.nasaapi.model.Result
import retrofit2.Response
import java.io.IOException

open class BaseRepository {
    suspend fun <T:Any> safeApiCalls(call: suspend()-> Response<T>, errorMsg: String):T?{
        val result = safeApiResult(call,errorMsg)
        var data: T? = null

        when(result){
            is Result.Success -> data=result.data
            is Result.Error -> {
                Log.d("EPIC",result.exception.toString())
            }
        }
        return data
    }
    private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>, errorMessage: String) : Result<T> {
        val response = call.invoke()
        if(response.isSuccessful) return Result.Success(response.body()!!)

        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}