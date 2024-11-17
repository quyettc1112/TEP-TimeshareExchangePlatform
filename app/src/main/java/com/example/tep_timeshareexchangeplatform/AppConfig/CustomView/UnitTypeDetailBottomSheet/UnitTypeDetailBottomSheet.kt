package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.UnitTypeDetailBottomSheet

import android.content.Context
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.displayBedsInfo
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.DialogUnitTypeDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class UnitTypeDetailBottomSheet(
    context: Context,
    private val unitType: MyExchangeRequestDetailResponse.RoomInfo.UnitType
) {
    private val dialog: BottomSheetDialog = BottomSheetDialog(context)
    private val binding: DialogUnitTypeDetailBinding =
        DialogUnitTypeDetailBinding.inflate(dialog.layoutInflater)

    init {
        dialog.setContentView(binding.root)
        bindData()
    }

    private fun bindData() {
        // Set Image
        Glide.with(binding.root.context)
            .load(unitType.photos) // URL hình ảnh
            .placeholder(R.drawable.backgroud_earth) // Placeholder khi tải ảnh
            .error(R.drawable.ic_error_) // Ảnh lỗi
            .into(binding.imUnitTypeDetail)

        // Set Title
        binding.tvTitleUnit.text = unitType.title

        // Beds
        val unitTypeMap = mapOf(
            "bedsFull" to unitType.bedsFull,
            "bedsKing" to unitType.bedsKing,
            "bedsSofa" to unitType.bedsSofa,
            "bedsMurphy" to unitType.bedsMurphy,
            "bedsQueen" to unitType.bedsQueen,
            "bedsTwin" to unitType.bedsTwin
        )
        binding.tvNumBed.text = unitType.bedrooms.toString()
        binding.tvBed.text = displayBedsInfo(unitTypeMap)

        // Kitchen
        binding.tvKitchen.text = unitType.kitchen
        binding.tvNumKitchen.text = "1"

        // Bathroom
        binding.tvNumBath.text = unitType.bathrooms.toString()

        // Max Guest
        binding.tvNumPerson.text = unitType.sleeps.toString()

        // Scope
        binding.tvScope.text = "Khu vực phân bổ: " + unitType.buildingsOption

        // Direction
        binding.tvDirection.text = "Hướng quan sát" + unitType.view

        // Set Description
        binding.tvDescription.text = unitType.description
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}