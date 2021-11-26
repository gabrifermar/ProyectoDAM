package com.gabrifermar.proyectodam.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Weather")
data class Weather(@PrimaryKey(autoGenerate = true) val id:Int=0,val date:String, val data:String, val metar:Boolean,val fav:Boolean)