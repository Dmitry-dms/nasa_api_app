package com.dms.nasaapi.data

import com.dms.nasaapi.model.Listing

abstract class BaseRepository {
    abstract fun  search(query:String): Listing<out Any>
}