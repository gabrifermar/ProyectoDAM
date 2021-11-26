package com.gabrifermar.proyectodam.model.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MetarAPI(@SerializedName("results") var results: Int,@SerializedName("data") var data: List<String>)
