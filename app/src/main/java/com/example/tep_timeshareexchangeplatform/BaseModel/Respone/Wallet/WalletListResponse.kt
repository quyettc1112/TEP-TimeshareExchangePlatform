package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet


import com.google.gson.annotations.SerializedName

/**
{
  "id": 7,
  "ownerId": 31,
  "availableMoney": 0,
  "createdAt": "21-10-2024 18:42:56",
  "updatedAt": "21-10-2024 18:42:56",
  "isActive": true,
  "type": "CUSTOMER_WALLET",
  "transactions": [
    {
      "id": "183e4f8c-ddf4-4d3e-9c0e-8175f130534d",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "21-10-2024 19:09:28"
    },
    {
      "id": "202459bc-aec6-4b45-be06-3a8179ac1563",
      "money": -119000,
      "description": "Thanh toán membership 6 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "21-10-2024 18:43:51"
    },
    {
      "id": "27791ed3-3036-420d-a8f6-e6f091247109",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "21-10-2024 19:30:22"
    },
    {
      "id": "3a06c96c-d632-4b38-a4e1-6f71f54a1f83",
      "money": -119000,
      "description": "Thanh toán membership 6 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:24:32"
    },
    {
      "id": "40eeba60-b62a-4874-905d-071f69c356db",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "21-10-2024 19:34:30"
    },
    {
      "id": "4653e771-203e-42d7-9085-b0656c0b3108",
      "money": -119000,
      "description": "Thanh toán membership 6 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:28:41"
    },
    {
      "id": "76e1973d-ad9d-428b-8b55-baa18086df72",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "21-10-2024 19:14:18"
    },
    {
      "id": "777a97bc-b0ea-4ca3-b189-75ff7b367840",
      "money": -119000,
      "description": "Thanh toán membership 6 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:38:49"
    },
    {
      "id": "7ddabdd2-8466-4776-9e04-fa2d241c4b08",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "21-10-2024 19:28:02"
    },
    {
      "id": "8b65abc4-f446-4536-b4a2-1ffea5045c13",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:18:45"
    },
    {
      "id": "b1218cfb-8320-4468-8d00-5124b5390bbd",
      "money": -119000,
      "description": "Thanh toán membership 6 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:20:48"
    },
    {
      "id": "b6ba90f7-1711-4bcb-a555-f3e20d2f659b",
      "money": -119000,
      "description": "Thanh toán membership 6 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:52:06"
    },
    {
      "id": "cda051d2-d26f-4650-a9f5-d4a86bd75791",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:14:44"
    },
    {
      "id": "dad58098-74e5-467a-a2b3-710673a2757a",
      "money": -239000,
      "description": "Thanh toán membership 12 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:31:06"
    },
    {
      "id": "de1a0207-2233-4a32-ac29-b9fe242b6815",
      "money": -119000,
      "description": "Thanh toán membership 6 tháng",
      "paymentMethod": "VNPAY",
      "createdAt": "22-10-2024 04:29:48"
    }
  ]
}
*/
data class WalletListResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("availableMoney") val availableMoney: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("type") val type: String,
    @SerializedName("transactions") val transactions: List<Transaction>
) {
    data class Transaction(
        @SerializedName("id") val id: String,
        @SerializedName("money") val money: Int,
        @SerializedName("description") val description: String,
        @SerializedName("paymentMethod") val paymentMethod: String,
        @SerializedName("createdAt") val createdAt: String
    )
}