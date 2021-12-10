package com.gabrifermar.proyectodam.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Weather::class], version = 1)
abstract class WeatherDB : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    private class WeatherDBCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var weatherDao = database.weatherDao()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WeatherDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDB::class.java,
                    "Weather"
                ).addCallback(WeatherDBCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}