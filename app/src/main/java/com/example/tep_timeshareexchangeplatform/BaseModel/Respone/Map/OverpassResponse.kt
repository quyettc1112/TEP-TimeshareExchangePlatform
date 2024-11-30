package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map


import com.google.gson.annotations.SerializedName

data class OverpassResponse(
    @SerializedName("version") val version: Double,
    @SerializedName("generator") val generator: String,
    @SerializedName("osm3s") val osm3s: Osm3s,
    @SerializedName("elements") val elements: List<Element>
) {
    data class Osm3s(
        @SerializedName("timestamp_osm_base") val timestampOsmBase: String,
        @SerializedName("copyright") val copyright: String
    )

    data class Element(
        @SerializedName("type") val type: String,
        @SerializedName("id") val id: Long,
        @SerializedName("lat") val lat: Double,
        @SerializedName("lon") val lon: Double,
        @SerializedName("tags") val tags: Tags
    ) {
        data class Tags(
            @SerializedName("name") val name: String,
            @SerializedName("name:vi") val nameVi: String?,
            @SerializedName("shop") val shop: String?,
            @SerializedName("addr:street") val addrStreet: String?,
            @SerializedName("amenity") val amenity: String?,
            @SerializedName("addr:city") val addrCity: String?,
            @SerializedName("addr:district") val addrDistrict: String?,
            @SerializedName("addr:housenumber") val addrHousenumber: String?,
            @SerializedName("addr:postcode") val addrPostcode: String?,
            @SerializedName("addr:province") val addrProvince: String?,
            @SerializedName("addr:subdistrict") val addrSubdistrict: String?,
            @SerializedName("description") val description: String?,
            @SerializedName("email") val email: String?,
            @SerializedName("office") val office: String?,
            @SerializedName("opening_hours") val openingHours: String?,
            @SerializedName("operator") val `operator`: String?,
            @SerializedName("payment:cash") val paymentCash: String?,
            @SerializedName("payment:credit_cards") val paymentCreditCards: String?,
            @SerializedName("phone") val phone: String?,
            @SerializedName("ref:vatin") val refVatin: String?,
            @SerializedName("start_date") val startDate: String?,
            @SerializedName("website") val website: String?,
            @SerializedName("religion") val religion: String?,
            @SerializedName("place") val place: String?,
            @SerializedName("craft") val craft: String?,
            @SerializedName("bus") val bus: String?,
            @SerializedName("highway") val highway: String?,
            @SerializedName("public_transport") val publicTransport: String?
        )
    }
}