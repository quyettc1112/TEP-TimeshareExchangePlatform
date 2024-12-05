package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.AmenitiesBottomSheetFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.mapRoomAmenitiesToAmenitiesModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.MyTimeshareDetailAcitivity.MyTimeshareDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.MyTimeshareDetailAcitivity.MyTimeshareDetailViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.databinding.DialogAmenitiesBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AmenitiesBottomSheetFragment(
    private val amenitiesList: List<MyTimeshareDetailResponse.RoomAmenity>,
    private val viewModel: MyTimeshareDetailViewModel,
    private val activity: MyTimeshareDetailActivity
) : BottomSheetDialogFragment() {

    private var featuresAdapter = AmenitiesAdapter()
    private var entertainmentAdapter = AmenitiesAdapter()
    private var kitchenAdapter = AmenitiesAdapter()
    private var policyAdapter = AmenitiesAdapter()
    private lateinit var binding: DialogAmenitiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAmenitiesBinding.inflate(inflater, container, false)
        initAdapter()
        Log.d("POssssssss_dilaog", amenitiesList.toString())
        eventClick()
        return binding.root
    }

    private fun initAdapter() {
        kitchenAdapter.apply {
            submitList(Constant.listAmenities)
            onItemChecked = {
                //  postingFlowViewModel.updateAmenitiesForType(AmenityType.KITCHEN, getCheckedItems())
            }
        }
        entertainmentAdapter.apply {
            submitList(Constant.listEntertament)
            onItemChecked = {
                /*postingFlowViewModel.updateAmenitiesForType(
                    AmenityType.ENTERTAINMENT,
                    getCheckedItems()
                )*/

            }
        }
        policyAdapter.apply {
            submitList(Constant.listPolicy)
            onItemChecked = {
                // postingFlowViewModel.updateAmenitiesForType(AmenityType.POLICY, getCheckedItems())
            }
        }
        featuresAdapter.apply {
            submitList(Constant.listFeatures)
            onItemChecked = {
                //  postingFlowViewModel.updateAmenitiesForType(AmenityType.FEATURES, getCheckedItems())

            }
        }


        val featuresList = mapRoomAmenitiesToAmenitiesModel(amenitiesList, AmenityType.FEATURES)
        val entertainmentList =
            mapRoomAmenitiesToAmenitiesModel(amenitiesList, AmenityType.ENTERTAINMENT)
        val policyList = mapRoomAmenitiesToAmenitiesModel(amenitiesList, AmenityType.POLICY)
        val kitchenList = mapRoomAmenitiesToAmenitiesModel(amenitiesList, AmenityType.KITCHEN)

        featuresAdapter.updateCheckedItemsFromList(featuresList)
        entertainmentAdapter.updateCheckedItemsFromList(entertainmentList)
        kitchenAdapter.updateCheckedItemsFromList(kitchenList)
        policyAdapter.updateCheckedItemsFromList(policyList)

        bindDataAmenities()
    }

    private fun bindDataAmenities() {
        // Set List Kitchen
        binding.rvKitchen.apply {
            adapter = kitchenAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Amenities Entertament
        binding.rvAmenitiesEntertainment.apply {
            adapter = entertainmentAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Policy
        binding.rvPolicy.apply {
            adapter = policyAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Features
        binding.rvFeatures.apply {
            adapter = featuresAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

    }
    private fun eventClick() {
        binding.btnEditAmenities.setOnClickListener {
            setEnableAllAmenities(true)
        }

        binding.btnSaveAmenities.setOnClickListener {
            if(viewModel.isValidSelection()) {
                activity.showConfirmDialog(
                    "Lưu thay đổi",
                    "Bạn có chắc chắn muốn lưu thay đổi không?",
                    "Lưu",
                    "Hủy",
                    "",
                   object : ConfirmDialog.ConfirmCallback {
                       override fun negativeAction() {

                       }

                       override fun positiveAction() {

                       }

                   }
                )
            }


            setEnableAllAmenities(false)
            viewModel.updateAmenitiesForType(AmenityType.FEATURES, featuresAdapter.getCheckedItems())
            viewModel.updateAmenitiesForType(
                AmenityType.ENTERTAINMENT,
                entertainmentAdapter.getCheckedItems()
            )
            viewModel.updateAmenitiesForType(AmenityType.KITCHEN, kitchenAdapter.getCheckedItems())
            viewModel.updateAmenitiesForType(AmenityType.POLICY, policyAdapter.getCheckedItems())
        }


    }


    private fun setEnableAllAmenities(isEnable: Boolean) {
        policyAdapter.setEnableCheckBoxClick(isEnable)
        featuresAdapter.setEnableCheckBoxClick(isEnable)
        kitchenAdapter.setEnableCheckBoxClick(isEnable)
        entertainmentAdapter.setEnableCheckBoxClick(isEnable)
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

    fun mapRoomAmenitiesToAmenitiesModel(
        inputList: List<MyTimeshareDetailResponse.RoomAmenity>,
        amenityType: AmenityType
    ): List<AmenitiesModel> {
        return inputList
            .filter { it.type == amenityType.name } // Lọc theo AmenityType
            .map { amenity ->
                AmenitiesModel(
                    name = amenity.name,
                    type = amenity.type,
                    isChecked = true
                )
            }
    }

}