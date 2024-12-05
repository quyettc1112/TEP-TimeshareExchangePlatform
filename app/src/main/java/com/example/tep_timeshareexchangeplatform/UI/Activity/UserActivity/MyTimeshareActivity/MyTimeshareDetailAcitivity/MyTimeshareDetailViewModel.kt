package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.MyTimeshareDetailAcitivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.RoomAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareUpdateDTO
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
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    // ----------------------------------------------------------//
    // Call API get my timeshare Detail
    private val _myTimeshareDetail = MutableLiveData<Resource<MyTimeshareDetailResponse>?>()
    val myTimeshareDetail: MutableLiveData<Resource<MyTimeshareDetailResponse>?> = _myTimeshareDetail
    fun getMyTimeshareDetail(token: String, timeshareID: Int) {
        viewModelScope.launch {
            _myTimeshareDetail.postValue(Resource.loading(null))
            timeshareRepository.getMyTimeshareDetail(token, timeshareID).let {
                _myTimeshareDetail.postValue(it)
            }
        }
    }

    // Lưu trạng thái tiện ích theo từng loại (type)
    private val _selectedAmenities = MutableLiveData<Map<AmenityType, List<AmenitiesModel>>?>()
    val selectedAmenities: MutableLiveData<Map<AmenityType, List<AmenitiesModel>>?> get() = _selectedAmenities


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



    private val _currentRoomInfo = MutableLiveData<Int?>()
    val currentRoomInfo: MutableLiveData<Int?>
        get() = _currentRoomInfo

    fun updateCurrentRoomInfo(currentRoomInfo: Int) {
        _currentRoomInfo.value = currentRoomInfo
    }

    // ----------------------------------------------------------//
    // Call Unit Type Detail
    // Init MutableLiveData for Unit Type Detail
    private val _unitTypeDetail = MutableLiveData<Resource<UnitTypeModel>?>()
    val unitTypeDetail: MutableLiveData<Resource<UnitTypeModel>?> = _unitTypeDetail
    fun getUnitTypeDetailByID(token: String, unitTypeID: Int) {
        viewModelScope.launch {
            _unitTypeDetail.postValue(Resource.loading(null))
            publicResortAPIRepository.getUnitTypeDetailById(token, unitTypeID).let {
                _unitTypeDetail.postValue(it)
            }
        }
    }

    private val _roomList = MutableLiveData<Resource<List<RoomModel>>?>()
    val roomList: MutableLiveData<Resource<List<RoomModel>>?> = _roomList

    // Function to get Room List By Resort ID
    fun getRoomListByResortId(token: String, resortID: Int) {
        viewModelScope.launch {
            _roomList.postValue(Resource.loading(null))
            roomAPIRepository.getRoomListByResortId(token, resortID).let {
                _roomList.postValue(it)
            }
        }
    }


    private val _startDateTimeshare = MutableLiveData<String?>()
    val startDateTimeshare: MutableLiveData<String?> get() = _startDateTimeshare

    private val _endDateTimeshare = MutableLiveData<String?>()
    val endDateTimeshare: MutableLiveData<String?> get() = _endDateTimeshare

    fun setTimeshareDateRange(start: Long?, end: Long?) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Định dạng chuẩn lưu trữ
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
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Định dạng lưu trữ
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Định dạng mong muốn

        val startDateFormatted = _startDateTimeshare.value?.let { dateString ->
            try {
                val date = inputFormat.parse(dateString) // Chuyển chuỗi lưu trữ sang Date
                date?.let { outputFormat.format(it) } ?: dateString // Định dạng lại
            } catch (e: Exception) {
                dateString // Nếu lỗi, trả về chuỗi ban đầu
            }
        } ?: ""

        val endDateFormatted = _endDateTimeshare.value?.let { dateString ->
            try {
                val date = inputFormat.parse(dateString) // Chuyển chuỗi lưu trữ sang Date
                date?.let { outputFormat.format(it) } ?: dateString // Định dạng lại
            } catch (e: Exception) {
                dateString // Nếu lỗi, trả về chuỗi ban đầu
            }
        } ?: ""

        return Pair(startDateFormatted, endDateFormatted)
    }


    private val _numberOfNightsTimeShare = MutableLiveData<Int?>()
    val numberOfNightsTimeshare: MutableLiveData<Int?> get() = _numberOfNightsTimeShare
    fun getNumberOfNightsTimeshare(): Int {
        return _numberOfNightsTimeShare.value ?: 0
    }

    fun resetTimeshareDateRange() {
        _startDateTimeshare.value = ""
        _endDateTimeshare.value = ""
        _numberOfNightsTimeShare.value = 0
    }


    private val _yearRange = MutableLiveData<Pair<Int, Int>?>()
    val yearRange: MutableLiveData<Pair<Int, Int>?> get() = _yearRange

    fun setYearRange(startYear: Int, endYear: Int) {
        _yearRange.value = Pair(startYear, endYear)
    }

    fun getYearRange(): Pair<Int, Int> {
        return (_yearRange.value ?: Pair(0, 0)) as Pair<Int, Int>
    }

    fun resetTimeshareYearRange() {
        _yearRange.value = Pair(0, 0)
    }


    // ----------------------------------------------------------//
    private val _updateTimeshare = MutableLiveData<Resource<Void>?>()
    val updateTimeshare: MutableLiveData<Resource<Void>?> get() = _updateTimeshare
    fun callUpdateTimeshare(token: String, timeshareID: Int, timeshareUpdateDTO: TimeshareUpdateDTO) {
        viewModelScope.launch {
            _updateTimeshare.postValue(Resource.loading(null))
            customerAPIRepository.updateTimeshare(token, timeshareID, timeshareUpdateDTO).let {
                _updateTimeshare.postValue(it)
            }
        }
    }

    fun resetAllValue() {
        _selectedAmenities.value = null
        _currentRoomInfo.value = null
        _unitTypeDetail.value = null
        _roomList.value = null
        _startDateTimeshare.value = null
        _endDateTimeshare.value = null
        _numberOfNightsTimeShare.value = null
        _yearRange.value = null
        _updateTimeshare.value = null
    }


}