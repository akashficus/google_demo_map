package com.rf.locationSource.localDB.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rf.locationSource.localDB.model.Place

@Dao
interface GoogleMapDemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: Place)

    @Transaction
    @Query("SELECT * FROM place ORDER BY placeDistance ASC")
    fun getPlaceList(): LiveData<Place>

    @Delete
    suspend fun deletePlace(place: Place)

    @Query("SELECT * FROM place ORDER BY placeDistance ASC")
    suspend fun getAllPlaces(): List<Place>

    @Query("Select * from place limit 1")
    suspend fun getFirstPlaceOrder() : Place?

}