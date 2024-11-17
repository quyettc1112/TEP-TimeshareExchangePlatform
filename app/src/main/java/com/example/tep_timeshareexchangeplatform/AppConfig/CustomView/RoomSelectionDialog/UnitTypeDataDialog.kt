package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.displayBedsInfo
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.DialogBottomSheetBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogUnitTypeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UnitTypeDataDialog (
    private val unitType: MyExchangePostingDetailResponse.UnitType
) : BottomSheetDialogFragment() {

    private var _binding: DialogUnitTypeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sử dụng View Binding để inflate layout
        _binding = DialogUnitTypeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(unitType: MyExchangePostingDetailResponse.UnitType): UnitTypeDataDialog {
            return UnitTypeDataDialog(unitType)
        }
    }

}