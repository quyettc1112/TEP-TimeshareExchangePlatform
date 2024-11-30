package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map


import com.google.gson.annotations.SerializedName

data class DirectionResponse(
    @SerializedName("code") val code: String,
    @SerializedName("routes") val routes: List<Route>,
    @SerializedName("waypoints") val waypoints: List<Waypoint>
) {
    data class Route(
        @SerializedName("geometry") val geometry: Geometry,
        @SerializedName("legs") val legs: List<Leg>,
        @SerializedName("weight_name") val weightName: String,
        @SerializedName("weight") val weight: Double,
        @SerializedName("duration") val duration: Double,
        @SerializedName("distance") val distance: Double
    ) {
        data class Geometry(
            @SerializedName("coordinates") val coordinates: List<List<Double>>,
            @SerializedName("type") val type: String
        )

        data class Leg(
            @SerializedName("steps") val steps: List<Any?>,
            @SerializedName("summary") val summary: String,
            @SerializedName("weight") val weight: Double,
            @SerializedName("duration") val duration: Double,
            @SerializedName("distance") val distance: Double
        )
    }

    data class Waypoint(
        @SerializedName("hint") val hint: String,
        @SerializedName("distance") val distance: Double,
        @SerializedName("name") val name: String,
        @SerializedName("location") val location: List<Double>
    )
}