package com.gabrifermar.proyectodam.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weather: List<Weather>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertone(weather: Weather)

    @Query("SELECT * FROM Weather WHERE metar = 1 ORDER BY id DESC LIMIT 50")
    suspend fun allMetar(): List<Weather>

    @Query("SELECT * FROM Weather WHERE metar = 0 ORDER BY id DESC LIMIT 50")
    suspend fun allTaf(): List<Weather>

    @Query("SELECT EXISTS(SELECT * FROM Weather WHERE data = :data)")
    suspend fun isExists(data: String): Boolean

    @Query("UPDATE Weather SET fav = 1 WHERE data = :data")
    suspend fun addFav(data: String)

    @Query("UPDATE Weather SET fav = 0 WHERE data = :data")
    suspend fun removeFav(data: String)

    @Query("SELECT * FROM Weather WHERE metar = 1 and fav = 1 ORDER BY id DESC LIMIT 50")
    suspend fun allMetarFav(): List<Weather>

    @Query("SELECT * FROM Weather WHERE metar = 0 and fav = 1 ORDER BY id DESC LIMIT 50")
    suspend fun allTafFav(): List<Weather>
    //fun allTafFav(): LiveData<List<Weather>>

    @Query("DELETE FROM Weather WHERE data = :data")
    suspend fun delete(data: String)

//    @Query("SELECT EXISTS(SELECT * FROM Weather WHERE data = :data and fav = 1)")
//    suspend fun isFav(data: String): Boolean

}