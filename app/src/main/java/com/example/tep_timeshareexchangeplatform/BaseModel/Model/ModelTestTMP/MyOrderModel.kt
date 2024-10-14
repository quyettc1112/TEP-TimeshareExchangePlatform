package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

data class MyOrderModel(
    val orderId: String, // Corresponding to `tv_order_code`
    val status: String, // Corresponding to `tv_status`
    val timeshareName: String, // Corresponding to `tv_timeshareName`
    val checkInDate: String, // Corresponding to `tv_checkin_date`
    val checkInDay: String, // Corresponding to `tv_checkin_day`
    val checkOutDate: String, // Corresponding to `tv_checkout_date`
    val checkOutDay: String, // Corresponding to `tv_checkout_day`
    val timeshareType: String, // Corresponding to `tv_timeshareType`
    val price: String, // Corresponding to `tv_price`
    val dateOfOrder: String, // Corresponding to `tv_checkin_date` in Time order
    val timeOfOrder: String, // Corresponding to `tv_checkout_date` in Time order
    val paymentTypeIcon: Int, // Corresponding to `im_tyep_payment` (image resource ID)
    val timeshareImage: Int // Corresponding to `im_imageTimeshare` (image resource ID)
)