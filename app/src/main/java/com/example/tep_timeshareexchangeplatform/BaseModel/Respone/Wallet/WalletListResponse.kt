package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet


import com.google.gson.annotations.SerializedName

/**
{
  "content": [
    {
      "id": "18a85ae5-a60e-4da4-a882-a0d22c9518b8",
      "walletId": 7,
      "money": -4500,
      "transactionType": "RENTALBOOKING",
      "description": "Thanh toán đặt chỗ timeshare cho thuê ",
      "paymentMethod": "WALLET",
      "createdAt": "06-11-2024 23:05:55",
      "fee": 0
    },
    {
      "id": "292df358-8608-4b60-a091-70ddd6e091a2",
      "walletId": 7,
      "money": -7044000,
      "transactionType": "RENTALBOOKING",
      "description": "Thanh toán đặt chỗ timeshare cho thuê ",
      "paymentMethod": "WALLET",
      "createdAt": "06-11-2024 23:04:48",
      "fee": 0
    },
    {
      "id": "fba9cf59-1e55-405d-8028-c9ebc4d5ea09",
      "walletId": 7,
      "money": -2250000,
      "transactionType": "RENTALBOOKING",
      "description": "Thanh toán đặt chỗ timeshare cho thuê ",
      "paymentMethod": "WALLET",
      "createdAt": "06-11-2024 23:03:50",
      "fee": 0
    },
    {
      "id": "05d4a9e1-89ac-45b8-a4ca-da56707e1904",
      "walletId": 7,
      "money": -5520000,
      "transactionType": "RENTALBOOKING",
      "description": "Thanh toán đặt chỗ timeshare cho thuê ",
      "paymentMethod": "WALLET",
      "createdAt": "06-11-2024 22:59:35",
      "fee": 0
    },
    {
      "id": "61ad10d6-937b-40be-b8fe-d7339b5f6dc8",
      "walletId": 7,
      "money": -239000,
      "transactionType": "MEMBERSHIP",
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "WALLET",
      "createdAt": "06-11-2024 15:57:08",
      "fee": 0
    },
    {
      "id": "e48d9073-327c-4b73-b82e-724d27ebf1e7",
      "walletId": 7,
      "money": 579000,
      "transactionType": "RENTALPOSTING",
      "description": "Giao dịch hoàn tiền từ chối chuyển nhượng quyền sở hữu timeshare gói 4",
      "paymentMethod": "WALLET",
      "createdAt": "06-11-2024 10:18:55",
      "fee": 0
    },
    {
      "id": "195cc748-2fc2-41b8-ae5a-13fa15842a29",
      "walletId": 7,
      "money": -599000,
      "transactionType": "RENTALPOSTING",
      "description": "Thanh toán đăng bài Gói Ủy Quyền",
      "paymentMethod": "WALLET",
      "createdAt": "05-11-2024 18:44:43",
      "fee": 0
    },
    {
      "id": "3411bccd-754a-48f9-89d9-577041c5c2fd",
      "walletId": 7,
      "money": -239000,
      "transactionType": "RENTALPOSTING",
      "description": "Thanh toán đăng bài Gói Premium",
      "paymentMethod": "WALLET",
      "createdAt": "05-11-2024 18:44:07",
      "fee": 0
    },
    {
      "id": "fe40101b-03af-4237-badf-06b23462309c",
      "walletId": 7,
      "money": -149000,
      "transactionType": "RENTALPOSTING",
      "description": "Thanh toán đăng bài Gói Cơ Bản",
      "paymentMethod": "WALLET",
      "createdAt": "04-11-2024 06:57:35",
      "fee": 0
    },
    {
      "id": "3cd2a623-df16-42c5-bf9b-203872bd3b9a",
      "walletId": 7,
      "money": -599000,
      "transactionType": "RENTALPOSTING",
      "description": "Thanh toán đăng bài Gói Ủy Quyền",
      "paymentMethod": "WALLET",
      "createdAt": "04-11-2024 06:37:59",
      "fee": 0
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 11,
  "totalElements": 103,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
*/
data class WalletListResponse(
    @SerializedName("content") val content: List<Content>,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("last") val last: Boolean,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("first") val first: Boolean,
    @SerializedName("empty") val empty: Boolean
) {
    data class Content(
        @SerializedName("id") val id: String,
        @SerializedName("walletId") val walletId: Int,
        @SerializedName("money") val money: Int,
        @SerializedName("transactionType") val transactionType: String,
        @SerializedName("description") val description: String,
        @SerializedName("paymentMethod") val paymentMethod: String,
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("fee") val fee: Int
    )

    data class Pageable(
        @SerializedName("pageNumber") val pageNumber: Int,
        @SerializedName("pageSize") val pageSize: Int,
        @SerializedName("sort") val sort: Sort,
        @SerializedName("offset") val offset: Int,
        @SerializedName("paged") val paged: Boolean,
        @SerializedName("unpaged") val unpaged: Boolean
    ) {
        data class Sort(
            @SerializedName("empty") val empty: Boolean,
            @SerializedName("sorted") val sorted: Boolean,
            @SerializedName("unsorted") val unsorted: Boolean
        )
    }

    data class Sort(
        @SerializedName("empty") val empty: Boolean,
        @SerializedName("sorted") val sorted: Boolean,
        @SerializedName("unsorted") val unsorted: Boolean
    )
}