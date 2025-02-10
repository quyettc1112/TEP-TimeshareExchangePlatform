package com.example.tep_timeshareexchangeplatform.API.BaseAPI

class BaseAPI {

    companion object {
        const val BASE_API : String = "http://35.247.160.131/api/"
        const val MOCK_API : String = ""
        const val OPEN_STREET_MAP_API : String = "https://nominatim.openstreetmap.org/"
        const val OVERPASS_API : String = "https://overpass-api.de/api/"
        const val ROUTING_API : String = "https://router.project-osrm.org/"
        const val NETWORK_TIMEOUT = 60L
    }
}