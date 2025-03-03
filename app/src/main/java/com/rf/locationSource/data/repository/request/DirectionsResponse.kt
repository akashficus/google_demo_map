package com.rf.locationSource.data.repository.request
/**
 * Represents the response received from the Google Directions API.
 * Contains a list of possible routes.
 */
data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val overview_polyline: OverviewPolyline
)

data class OverviewPolyline(
    val points: String
)

