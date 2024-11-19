package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room


import com.example.tep_timeshareexchangeplatform.Until.ObjectLike.AmenityLike
import com.google.gson.annotations.SerializedName

/**
{
  "roomId": 58,
  "roomInfoCode": "P.721",
  "isActive": true,
  "resortId": 1,
  "roomName": "Phòng 721",
  "status": "Available",
  "createdAt": "19-11-2024 10:25:39",
  "roomAmenities": [
    {
      "name": "Máy Điều Hòa",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Wifi",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Nước nóng/lạnh",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Nước uống miễn phí",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Sân hiên hoặc Ban Công",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Bàn ăn",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Bàn làm việc",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Máy giặt và máy sấy (trong căn hộ)",
      "type": "FEATURES",
      "isActive": true
    },
    {
      "name": "Máy phát DVD",
      "type": "ENTERTAINMENT",
      "isActive": true
    },
    {
      "name": "Quầy Bar",
      "type": "ENTERTAINMENT",
      "isActive": true
    },
    {
      "name": "Máy Chiếu Phim",
      "type": "ENTERTAINMENT",
      "isActive": true
    },
    {
      "name": "Mạng Internet",
      "type": "ENTERTAINMENT",
      "isActive": true
    },
    {
      "name": "Radio",
      "type": "ENTERTAINMENT",
      "isActive": true
    },
    {
      "name": "TV thông minh",
      "type": "ENTERTAINMENT",
      "isActive": true
    },
    {
      "name": "Không hút thuốc",
      "type": "POLICY",
      "isActive": true
    },
    {
      "name": "Không thú cưng",
      "type": "POLICY",
      "isActive": true
    },
    {
      "name": "Độ tuổi tối thiểu để nhận phòng: 18",
      "type": "POLICY",
      "isActive": true
    },
    {
      "name": "Máy pha cà phê",
      "type": "KITCHEN",
      "isActive": true
    },
    {
      "name": "Lò vi sóng",
      "type": "KITCHEN",
      "isActive": true
    },
    {
      "name": "Máy rửa chén",
      "type": "KITCHEN",
      "isActive": true
    },
    {
      "name": "Máy nướng bánh mì",
      "type": "KITCHEN",
      "isActive": true
    },
    {
      "name": "Tủ lạnh (lớn)",
      "type": "KITCHEN",
      "isActive": true
    },
    {
      "name": "Tủ lạnh (nhỏ)",
      "type": "KITCHEN",
      "isActive": true
    },
    {
      "name": "Bếp lò",
      "type": "KITCHEN",
      "isActive": true
    }
  ]
}
*/
data class RoomDetailResponse(
    @SerializedName("roomId") val roomId: Int,
    @SerializedName("roomInfoCode") val roomInfoCode: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("resortId") val resortId: Int,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("roomAmenities") val roomAmenities: List<RoomAmenity>
) {
    data class RoomAmenity(
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String,
        @SerializedName("isActive") val isActive: Boolean
    )
}