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
    @Query("SELECT * FROM place")
    fun getPlaceList(): LiveData<Place>

    @Delete
    fun deletePlace(place: Place)

    @Query("SELECT * FROM Place")
    fun getAllPlaces(): List<Place>

}