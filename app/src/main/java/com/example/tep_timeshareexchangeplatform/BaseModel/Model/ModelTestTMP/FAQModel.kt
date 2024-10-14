package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

data class FAQModel(
    val id: Int,
    val title: String,
    val desc: String,
    var isExpandable: Boolean = false
)
