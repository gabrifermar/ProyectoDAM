package com.gabrifermar.proyectodam.model.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.*

class WeatherRepository(private val weatherDao: WeatherDao) {

    @WorkerThread
    suspend fun allMetar(): List<Weather> {
        return weatherDao.allMetar()
    }

    @WorkerThread
    suspend fun allTaf(): List<Weather> {
        return weatherDao.allTaf()
    }

    @WorkerThread
    suspend fun insert(weather: List<Weather>) {
        weatherDao.insert(weather)
    }

    @WorkerThread
    suspend fun insertone(weather: Weather) {
        weatherDao.insertone(weather)
    }

    @WorkerThread
    suspend fun isExists(data: String): Boolean {
        return weatherDao.isExists(data)
    }

    @WorkerThread
    fun addFav(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.addFav(data)
        }
    }

    @WorkerThread
    fun removeFav(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.removeFav(data)
        }
    }

    @WorkerThread
    suspend fun allMetarFav(): List<Weather> {
        return weatherDao.allMetarFav()
    }

    @WorkerThread
    suspend fun allTafFav(): List<Weather> {
        return weatherDao.allTafFav()
    }

    @WorkerThread
    suspend fun delete(data: String) {
        weatherDao.delete(data)
    }
}