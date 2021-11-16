package com.gabrifermar.proyectodam

import androidx.annotation.Keep
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
@Keep
interface API {
    @GET
    suspend fun getMetar(@Url url:String):Response<MetarAPI>
}