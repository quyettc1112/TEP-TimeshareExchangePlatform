package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment


import com.google.gson.annotations.SerializedName

/**
{
  "url": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=1240000&vnp_Command=pay&vnp_CreateDate=20241020103410&vnp_CurrCode=VND&vnp_ExpireDate=20241020104910&vnp_IpAddr=42.114.200.150&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang%3A05416960&vnp_OrderType=asd&vnp_ReturnUrl=https%3A%2F%2Ffams-management.tech%2Fapi%2Fpayment%2Fpayment-infor&vnp_TmnCode=M60EWFNQ&vnp_TxnRef=05416960&vnp_Version=2.1.0&vnp_SecureHash=7a00c0dc24be7759ad944f46e29ddc11613ea01c614b2925bb06dfbeb7d837ba601051609ccbdb0e36d4498ad9b491baccb187e3be326d10d57999fda24c735f"
}
*/
data class PaymentResponse(
    @SerializedName("url") val url: String
)