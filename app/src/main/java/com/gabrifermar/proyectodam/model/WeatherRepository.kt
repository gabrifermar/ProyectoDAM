package com.gabrifermar.proyectodam.model

import android.os.Looper
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.CoroutinesRoom
import kotlinx.coroutines.*
import java.util.concurrent.Flow
import java.util.logging.Handler

class WeatherRepository(private val weatherDao: WeatherDao) {

    //val allTafFav: LiveData<List<Weather>> = weatherDao.allTafFav()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun allMetar(): List<Weather> {
        return weatherDao.allMetar()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun allTaf(): List<Weather> {
        return weatherDao.allTaf()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(weather: List<Weather>) {
        weatherDao.insert(weather)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertone(weather: Weather) {
        weatherDao.insertone(weather)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun isExists(data: String): Boolean {
        return weatherDao.isExists(data)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun addFav(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.addFav(data)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun removeFav(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.removeFav(data)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun allMetarFav(): List<Weather> {
        return weatherDao.allMetarFav()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun allTafFav(): List<Weather> {
        return weatherDao.allTafFav()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(data: String) {
        weatherDao.delete(data)
    }

}