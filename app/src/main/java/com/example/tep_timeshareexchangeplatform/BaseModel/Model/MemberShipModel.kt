package com.example.tep_timeshareexchangeplatform.BaseModel.Model

data class MemberShipModel(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val duration: Int,
    val type: String,
    val listBenefit: List<String>
)