package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTimeshareDetailViewModel @Inject constructor(
    private val timeshareRepository: TimeshareRepository
): ViewModel() {

    // ----------------------------------------------------------//
    // Call API get my timeshare Detail
    private val _myTimeshareDetail = MutableLiveData<Resource<MyTimeshareDetailResponse>>()
    val myTimeshareDetail: MutableLiveData<Resource<MyTimeshareDetailResponse>> = _myTimeshareDetail
    fun getMyTimeshareDetail(token: String, timeshareID: Int) {
        viewModelScope.launch {
            _myTimeshareDetail.postValue(Resource.loading(null))
            timeshareRepository.getMyTimeshareDetail(token, timeshareID).let {
                _myTimeshareDetail.postValue(it)
            }
        }
    }

    // Lưu trạng thái tiện ích theo từng loại (type)
    private val _selectedAmenities = MutableLiveData<Map<AmenityType, List<AmenitiesModel>>>()
    val selectedAmenities: LiveData<Map<AmenityType, List<AmenitiesModel>>> get() = _selectedAmenities


    fun updateAmenitiesForType(type: AmenityType, selectedAmenities: List<AmenitiesModel>) {
        val currentMap = _selectedAmenities.value?.toMutableMap() ?: mutableMapOf()
        currentMap[type] = selectedAmenities // Lưu toàn bộ danh sách, không chỉ các mục được chọn
        _selectedAmenities.value = currentMap
        Log.d("ViewModelUpdate", "Updated Type: $type, Data: ${currentMap[type]}")
    }

    fun getSelectedAmenitiesForPost(): List<RoomDTO.RoomAmenity> {
        return _selectedAmenities.value
            ?.flatMap { (type, amenities) ->
                amenities.filter { it.isChecked }.map { RoomDTO.RoomAmenity(it.name, type.name) }
            }
            ?: emptyList()
    }
    fun isValidSelection(): Boolean {
        // Nhóm danh sách các mục đã chọn theo `type`
        val selectedAmenities: List<RoomDTO.RoomAmenity> = getSelectedAmenitiesForPost()
        val groupedAmenities = selectedAmenities.groupBy { it.type }

        // Kiểm tra từng loại (FEATURES, ENTERTAINMENT, KITCHEN)
        val isFeaturesValid = groupedAmenities["FEATURES"]?.size ?: 0 >= 2
        val isEntertainmentValid = groupedAmenities["ENTERTAINMENT"]?.size ?: 0 >= 2
        val isKitchenValid = groupedAmenities["KITCHEN"]?.size ?: 0 >= 2

        // Không cần kiểm tra POLICY
        return isFeaturesValid && isEntertainmentValid && isKitchenValid
    }

    fun clearAllAmenities() {
        _selectedAmenities.value = AmenityType.values().associateWith { emptyList() }
    }

}