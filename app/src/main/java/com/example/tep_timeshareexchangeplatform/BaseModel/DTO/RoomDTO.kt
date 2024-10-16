package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "roomInfoCode": "user_create_5",
  "isActive": true,
  "resortId": 1,
  "status": "string",
  "unitTypeId": 1,
  "roomName": "Phòng Vua Chúa",
  "roomAmenities": [
    {
      "name": "string",
      "type": "string"
    }
  ]
}
*/
data class RoomDTO(
    @SerializedName("roomInfoCode") val roomInfoCode: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("resortId") val resortId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("unitTypeId") val unitTypeId: Int,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("roomAmenities") val roomAmenities: List<RoomAmenity>
) {
    data class RoomAmenity(
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )
}