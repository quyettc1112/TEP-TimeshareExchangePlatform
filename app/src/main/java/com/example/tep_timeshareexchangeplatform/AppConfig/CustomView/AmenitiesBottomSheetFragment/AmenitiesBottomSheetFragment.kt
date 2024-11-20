package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.AmenitiesBottomSheetFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.databinding.DialogAmenitiesBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AmenitiesBottomSheetFragment(
    private val amenitiesList: List<AmenitiesModel>
) : BottomSheetDialogFragment() {

    private var featuresAdapter = RoomAmenitiesAdapter()
    private var entertainmentAdapter = RoomAmenitiesAdapter()
    private var kitchenAdapter = RoomAmenitiesAdapter()
    private var policyAdapter = RoomAmenitiesAdapter()
    private lateinit var binding: DialogAmenitiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAmenitiesBinding.inflate(inflater, container, false)
        initAdapter()
        bindData()
        Log.d("POssssssss_dilaog", amenitiesList.toString())

        return binding.root
    }

    private fun initAdapter() {
        featuresAdapter.submitOriginalList(amenitiesList)
        featuresAdapter.filterByAmenityTypes(AmenityType.FEATURES)

        entertainmentAdapter.submitOriginalList(amenitiesList)
        entertainmentAdapter.filterByAmenityTypes(AmenityType.ENTERTAINMENT)

        kitchenAdapter.submitOriginalList(amenitiesList)
        kitchenAdapter.filterByAmenityTypes(AmenityType.KITCHEN)

        policyAdapter.submitOriginalList(amenitiesList)
        policyAdapter.filterByAmenityTypes(AmenityType.POLICY)

    }

    private fun bindData() {
        binding.rvFeatures.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter =  featuresAdapter
        }

        binding.rvAmenitiesEntertainment.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP            // Cho phép các mục xuống dòng nếu không đủ chỗ
            }
            adapter = entertainmentAdapter
        }

        binding.rvKitchen.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = kitchenAdapter
        }

        binding.rvPolicy.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = policyAdapter
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.CENTER)
        }
    }
}