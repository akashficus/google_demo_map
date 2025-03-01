package com.rf.locationSource.localDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "place")
data class Place(
    @PrimaryKey
    val placeId:String,
    var placeName: String?=null,
    var placeAddress: String?=null,
    var placeLatitude: Double?=null,
    var placeLongitude: Double?=null
)
