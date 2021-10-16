package com.gabrifermar.proyectodam

import com.google.gson.annotations.SerializedName

data class MetarAPI(@SerializedName("results") var results: Int,@SerializedName("data") var data: List<String>)
