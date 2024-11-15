package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking


import com.google.gson.annotations.SerializedName

/**
{
  "id": 5,
  "rentalPosting": {
    "id": 19,
    "description": "test",
    "isVerify": false,
    "isBookable": true,
    "roomInfo": {
      "id": 2,
      "roomInfoCode": "123",
      "roomInfoName": "cc",
      "isActive": true,
      "status": "booking",
      "unitType": {
        "id": 1,
        "title": "Phòng Queen",
        "area": "string",
        "bathrooms": 1,
        "bedrooms": 2,
        "bedsFull": 0,
        "bedsKing": 0,
        "bedsSofa": 0,
        "bedsMurphy": 0,
        "bedsQueen": 0,
        "bedsTwin": 2,
        "buildingsOption": "string",
        "price": 500000,
        "description": "string",
        "kitchen": "Bếp chung",
        "photos": "string",
        "resortId": 1,
        "resortResortName": "Khách sạn Cương Quyết Ngầu Nhất Việt Nam",
        "resortLogo": "https://i.pinimg.com/564x/f3/7b/77/f37b774fd4d482b27e82021a4f862ae6.jpg",
        "resortAddress": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
        "resortDescription": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2.\nQuay tiếp tân 24 giờ luôn sẵn sàng phục vụ quý khách từ thủ tục nhận phòng đến trả phòng hay bất kỳ yêu cầu nào.\nNếu cần giúp đỡ xin hãy liên hệ đội ngũ tiếp tân, chúng tôi luôn sẵn sàng hỗ trợ quý khách.\nSóng WiFi phủ khắp các khu vực chung của khách sạn cho phép quý khách luôn kết nối với gia đình và bè bạn. ",
        "sleeps": 4,
        "view": "string",
        "isActive": true
      }
    },
    "cancellationType": {
      "id": 1,
      "name": "Flexible",
      "refundRate": 100,
      "durationBefore": 60,
      "description": "100% refund up to 60 days before check-in",
      "isActive": true
    },
    "rentalPackageId": 2,
    "rentalPackageRentalPackageName": "Gói Nâng Cao",
    "rentalPackageType": "Standard",
    "rentalPackagePrice": 199000,
    "createdDate": 1730232253000,
    "updatedDate": 1730874057000
  },
  "status": "Booked",
  "checkinDate": "04-11-2024",
  "checkoutDate": "06-11-2024",
  "primaryGuestName": "",
  "primaryGuestPhone": "string",
  "primaryGuestEmail": "string",
  "isActive": true,
  "isFeedback": false,
  "renterFullLegalName": "thanhlong",
  "renterLegalPhone": "string",
  "renterLegalAvatar": null,
  "serviceFee": 0,
  "totalPrice": 200,
  "totalNights": 4,
  "pricePerNights": 50,
  "createdDate": "05-11-2024 19:07:36",
  "updatedDate": "06-11-2024 06:22:19"
}
*/
data class MyBookingDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("rentalPosting") val rentalPosting: RentalPosting,
    @SerializedName("status") val status: String,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("primaryGuestName") val primaryGuestName: String,
    @SerializedName("primaryGuestPhone") val primaryGuestPhone: String,
    @SerializedName("primaryGuestEmail") val primaryGuestEmail: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isFeedback") val isFeedback: Boolean,
    @SerializedName("renterFullLegalName") val renterFullLegalName: String,
    @SerializedName("renterLegalPhone") val renterLegalPhone: String,
    @SerializedName("renterLegalAvatar") val renterLegalAvatar: Any?,
    @SerializedName("serviceFee") val serviceFee: Int,
    @SerializedName("totalPrice") val totalPrice: Int,
    @SerializedName("totalNights") val totalNights: Int,
    @SerializedName("pricePerNights") val pricePerNights: Int,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("source") val source: String
) {
    data class RentalPosting(
        @SerializedName("id") val id: Int,
        @SerializedName("description") val description: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("isBookable") val isBookable: Boolean,
        @SerializedName("roomInfo") val roomInfo: RoomInfo,
        @SerializedName("cancellationType") val cancellationType: CancellationType,
        @SerializedName("rentalPackageId") val rentalPackageId: Int,
        @SerializedName("rentalPackageRentalPackageName") val rentalPackageRentalPackageName: String,
        @SerializedName("rentalPackageType") val rentalPackageType: String,
        @SerializedName("rentalPackagePrice") val rentalPackagePrice: Int,
        @SerializedName("createdDate") val createdDate: Long,
        @SerializedName("updatedDate") val updatedDate: Long
    ) {
        data class RoomInfo(
            @SerializedName("id") val id: Int,
            @SerializedName("roomInfoCode") val roomInfoCode: String,
            @SerializedName("roomInfoName") val roomInfoName: String,
            @SerializedName("isActive") val isActive: Boolean,
            @SerializedName("status") val status: String,
            @SerializedName("unitType") val unitType: UnitType
        ) {
            data class UnitType(
                @SerializedName("id") val id: Int,
                @SerializedName("title") val title: String,
                @SerializedName("area") val area: String,
                @SerializedName("bathrooms") val bathrooms: Int,
                @SerializedName("bedrooms") val bedrooms: Int,
                @SerializedName("bedsFull") val bedsFull: Int,
                @SerializedName("bedsKing") val bedsKing: Int,
                @SerializedName("bedsSofa") val bedsSofa: Int,
                @SerializedName("bedsMurphy") val bedsMurphy: Int,
                @SerializedName("bedsQueen") val bedsQueen: Int,
                @SerializedName("bedsTwin") val bedsTwin: Int,
                @SerializedName("buildingsOption") val buildingsOption: String,
                @SerializedName("price") val price: Int,
                @SerializedName("description") val description: String,
                @SerializedName("kitchen") val kitchen: String,
                @SerializedName("photos") val photos: String,
                @SerializedName("resortId") val resortId: Int,
                @SerializedName("resortResortName") val resortResortName: String,
                @SerializedName("resortLogo") val resortLogo: String,
                @SerializedName("resortAddress") val resortAddress: String,
                @SerializedName("resortDescription") val resortDescription: String,
                @SerializedName("sleeps") val sleeps: Int,
                @SerializedName("view") val view: String,
                @SerializedName("isActive") val isActive: Boolean
            )
        }

        data class CancellationType(
            @SerializedName("id") val id: Int,
            @SerializedName("name") val name: String,
            @SerializedName("refundRate") val refundRate: Int,
            @SerializedName("durationBefore") val durationBefore: Int,
            @SerializedName("description") val description: String,
            @SerializedName("isActive") val isActive: Boolean
        )
    }
}