package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.RoomAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MyTimeshareDetailViewModel @Inject constructor(
    private val timeshareRepository: TimeshareRepository,
    private val publicResortAPIRepository: PublicResortAPIRepository,
    private val roomAPIRepository: RoomAPIRepository,
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



    private val _currentRoomInfo = MutableLiveData<Int>()
    val currentRoomInfo: MutableLiveData<Int>
        get() = _currentRoomInfo

    fun updateCurrentRoomInfo(currentRoomInfo: Int) {
        _currentRoomInfo.value = currentRoomInfo
    }

    // ----------------------------------------------------------//
    // Call Unit Type Detail
    // Init MutableLiveData for Unit Type Detail
    private val _unitTypeDetail = MutableLiveData<Resource<UnitTypeModel>>()
    val unitTypeDetail: MutableLiveData<Resource<UnitTypeModel>> = _unitTypeDetail
    fun getUnitTypeDetailByID(token: String, unitTypeID: Int) {
        viewModelScope.launch {
            _unitTypeDetail.postValue(Resource.loading(null))
            publicResortAPIRepository.getUnitTypeDetailById(token, unitTypeID).let {
                _unitTypeDetail.postValue(it)
            }
        }
    }

    private val _roomList = MutableLiveData<Resource<List<RoomModel>>>()
    val roomList: MutableLiveData<Resource<List<RoomModel>>> = _roomList

    // Function to get Room List By Resort ID
    fun getRoomListByResortId(token: String, resortID: Int) {
        viewModelScope.launch {
            _roomList.postValue(Resource.loading(null))
            roomAPIRepository.getRoomListByResortId(token, resortID).let {
                _roomList.postValue(it)
            }
        }
    }


    private val _startDateTimeshare = MutableLiveData<String>()
    val startDateTimeshare: LiveData<String> get() = _startDateTimeshare

    private val _endDateTimeshare = MutableLiveData<String>()
    val endDateTimeshare: LiveData<String> get() = _endDateTimeshare

    fun setTimeshareDateRange(start: Long?, end: Long?) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        _startDateTimeshare.value = start?.let { dateFormat.format(Date(it)) }
        _endDateTimeshare.value = end?.let { dateFormat.format(Date(it)) }
        if (start != null && end != null) {
            val numberOfDays = ((end - start) / (1000 * 60 * 60 * 24)).toInt()
            _numberOfNightsTimeShare.value = numberOfDays
        } else {
            _numberOfNightsTimeShare.value = 0
        }
    }

    fun getTimeshareDateRange(): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Định dạng ban đầu
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Định dạng mong muốn

        val startDateFormatted = _startDateTimeshare.value?.let { dateString ->
            try {
                val date = inputFormat.parse(dateString) // Chuyển chuỗi sang Date
                date?.let { outputFormat.format(it) } ?: dateString // Định dạng lại
            } catch (e: Exception) {
                dateString // Trả về chuỗi gốc nếu có lỗi
            }
        } ?: ""

        val endDateFormatted = _endDateTimeshare.value?.let { dateString ->
            try {
                val date = inputFormat.parse(dateString) // Chuyển chuỗi sang Date
                date?.let { outputFormat.format(it) } ?: dateString // Định dạng lại
            } catch (e: Exception) {
                dateString // Trả về chuỗi gốc nếu có lỗi
            }
        } ?: ""

        return Pair(startDateFormatted, endDateFormatted)
    }


    private val _numberOfNightsTimeShare = MutableLiveData<Int>()
    val numberOfNightsTimeshare: LiveData<Int> get() = _numberOfNightsTimeShare
    fun getNumberOfNightsTimeshare(): Int {
        return _numberOfNightsTimeShare.value ?: 0
    }

    fun resetTimeshareDateRange() {
        _startDateTimeshare.value = ""
        _endDateTimeshare.value = ""
        _numberOfNightsTimeShare.value = 0
    }


    private val _yearRange = MutableLiveData<Pair<Int, Int>>()
    val yearRange: LiveData<Pair<Int, Int>> get() = _yearRange

    fun setYearRange(startYear: Int, endYear: Int) {
        _yearRange.value = Pair(startYear, endYear)
    }

    fun getYearRange(): Pair<Int, Int> {
        return (_yearRange.value ?: Pair(0, 0)) as Pair<Int, Int>
    }

    fun resetTimeshareYearRange() {
        _yearRange.value = Pair(0, 0)
    }

}