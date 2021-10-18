package com.gabrifermar.proyectodam

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface API {
    @GET
    suspend fun getMetar(@Url url:String):Response<MetarAPI>
}