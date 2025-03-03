package com.rf.locationSource.data.repository.request
/**
 * Represents the response received from the Google Place Details API.
 * Contains detailed information about a place.
 */
data class PlaceDetailsResponse(
    val result: PlaceResult
)

data class PlaceResult(
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)