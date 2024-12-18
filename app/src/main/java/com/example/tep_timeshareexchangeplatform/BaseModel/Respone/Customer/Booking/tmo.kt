package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking


import com.google.gson.annotations.SerializedName

/**
{"location": {
        "name": "test",
        "displayName": "Nguyễn Tất Thành, Phường Cam Nghĩa, Thành phố Cam Ranh, Huyện Cam Lâm, Khánh Hòa, Việt Nam",
        "latitude": "12.05476",
        "longitude": "109.19978",
        "country": "Vietnam",
        "placeId": null
      }}
*/
data class tmo(
    @SerializedName("location") val location: Location
) {
    data class Location(
        @SerializedName("name") val name: String?,
        @SerializedName("displayName") val displayName: String?,
        @SerializedName("latitude") val latitude: String,
        @SerializedName("longitude") val longitude: String,
        @SerializedName("country") val country: String?,
        @SerializedName("placeId") val placeId: Int?
    )
}