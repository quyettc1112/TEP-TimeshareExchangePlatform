package com.example.tep_timeshareexchangeplatform.BaseModel.Model

data class MyTimeshareModel(
    val id: Int,
    val name: String,
    val roomName: String,
    val checkInDate: String,
    val checkOutDate: String,
    val numberOfNight: Int,
    val price: String,
    val image: String
)
