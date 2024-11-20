package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "roomInfoAmenities": [
    {
      "name": "string",
      "type": "string"
    }
  ]
}
*/
data class RoomAmenitiesDTO(
    @SerializedName("roomInfoAmenities") val roomInfoAmenities: List<RoomInfoAmenity>
) {
    data class RoomInfoAmenity(
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )
}