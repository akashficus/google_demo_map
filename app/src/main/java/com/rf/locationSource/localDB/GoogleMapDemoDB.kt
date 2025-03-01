package com.rf.locationSource.localDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rf.locationSource.localDB.dao.GoogleMapDemoDao
import com.rf.locationSource.localDB.model.Place

@Database(
    entities = [
        Place::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GoogleMapDemoDB : RoomDatabase() {
    abstract fun googleMapDemoDao(): GoogleMapDemoDao
    companion object {
        @Volatile
        private var INSTANCE: GoogleMapDemoDB? = null
        private const val DATABASE_NAME = "googleMapDemoDB.db"

        // Returns the single instance of the GoogleMapDemo
        fun getDatabase(context: Context): GoogleMapDemoDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoogleMapDemoDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}