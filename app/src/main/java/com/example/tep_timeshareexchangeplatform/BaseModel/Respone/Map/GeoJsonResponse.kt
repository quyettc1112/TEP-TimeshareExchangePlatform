package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map


import com.google.gson.annotations.SerializedName

/**
{
    "type": "FeatureCollection",
    "licence": "Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright",
    "features": [
        {
            "type": "Feature",
            "properties": {
                "place_id": 238713274,
                "osm_type": "way",
                "osm_id": 1151316824,
                "place_rank": 30,
                "category": "building",
                "type": "hotel",
                "importance": 5.386053253981868e-05,
                "addresstype": "building",
                "name": "Khách Sạn Mường Thanh",
                "display_name": "Khách Sạn Mường Thanh, 81, Nguyễn Tất Thành, Phường Tự An, Thành phố Buôn Ma Thuột, Tỉnh Đắk Lắk, 63119, Việt Nam",
                "address": {
                    "building": "Khách Sạn Mường Thanh",
                    "house_number": "81",
                    "road": "Nguyễn Tất Thành",
                    "suburb": "Phường Tự An",
                    "city": "Thành phố Buôn Ma Thuột",
                    "state": "Tỉnh Đắk Lắk",
                    "ISO3166-2-lvl4": "VN-33",
                    "postcode": "63119",
                    "country": "Việt Nam",
                    "country_code": "vn"
                }
            },
            "bbox": [
                108.0625804,
                12.6923045,
                108.0635546,
                12.6932999
            ],
            "geometry": {
                "type": "Point",
                "coordinates": [
                    108.06307161563717,
                    12.69279795
                ]
            }
        }
    ]
}
*/
data class GeoJsonResponse(
    @SerializedName("type") val type: String,
    @SerializedName("licence") val licence: String,
    @SerializedName("features") val features: List<Feature>
) {
    data class Feature(
        @SerializedName("type") val type: String,
        @SerializedName("properties") val properties: Properties,
        @SerializedName("bbox") val bbox: List<Double>,
        @SerializedName("geometry") val geometry: Geometry
    ) {
        data class Properties(
            @SerializedName("place_id") val placeId: Int,
            @SerializedName("osm_type") val osmType: String,
            @SerializedName("osm_id") val osmId: Long,
            @SerializedName("place_rank") val placeRank: Int,
            @SerializedName("category") val category: String,
            @SerializedName("type") val type: String,
            @SerializedName("importance") val importance: Double,
            @SerializedName("addresstype") val addresstype: String,
            @SerializedName("name") val name: String,
            @SerializedName("display_name") val displayName: String,
            @SerializedName("address") val address: Address
        ) {
            data class Address(
                @SerializedName("building") val building: String,
                @SerializedName("house_number") val houseNumber: String,
                @SerializedName("road") val road: String,
                @SerializedName("suburb") val suburb: String,
                @SerializedName("city") val city: String,
                @SerializedName("state") val state: String,
                @SerializedName("ISO3166-2-lvl4") val iSO31662Lvl4: String,
                @SerializedName("postcode") val postcode: String,
                @SerializedName("country") val country: String,
                @SerializedName("country_code") val countryCode: String
            )
        }

        data class Geometry(
            @SerializedName("type") val type: String,
            @SerializedName("coordinates") val coordinates: List<Double>
        )
    }
}