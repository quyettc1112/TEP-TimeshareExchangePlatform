package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.AuthAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.SaveTokenDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Feedback.FeedbackResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.UserJWTPayloadModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository,
    private val authAPIRepository: AuthAPIRepository
) : ViewModel() {

    /**
     * Tracking Location
     *
     * This function is responsible for tracking the location of the user selected.
     */
    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location
    fun updateLocation(location: String) {
        _location.value = location
    }
    fun getLocation(): String {
        return _location.value ?: "Thành Phố Hồ Chí Minh"
    }


    /**
     * Tracking User Login State
     *
     * This function is responsible for tracking the user's login state.
     */
    private val _userJWTPayload = MutableLiveData<UserJWTPayloadModel>()
    val userJWTPayload: LiveData<UserJWTPayloadModel> = _userJWTPayload

    // Tracking User Login State
    private val _userLogState = MutableLiveData<UserLogState>()
    val userLogState: LiveData<UserLogState> = _userLogState
    fun setUserLogState(state: UserLogState) {
        _userLogState.value = state
    }

    // Tracking Customer Info
    private val _customerProfileInfo = MutableLiveData<CustomerProfileResponse>()
    val customerProfileInfo: LiveData<CustomerProfileResponse> = _customerProfileInfo
    fun setCustomerInfo(customerProfileResponse: CustomerProfileResponse) {
        _customerProfileInfo.value = customerProfileResponse
    }


    /**
     * Tracking Date Range
     *
     * This function is responsible for tracking the dateRange of User selected.
     */
    private val _dateRange = MutableLiveData<String>()
    val dateRange: LiveData<String> = _dateRange
    fun updateDateRange(dateRange: String) {
        _dateRange.value = dateRange
    }
    fun getDateRange(): String {
        return _dateRange.value ?: "20/10/2021 - 25/10/2021"
    }


    /**
     * Tracking Room Count, Adult Count, Children Count
     *
     * This function is responsible for tracking the roomCount, adultCount, childrenCount of User selected.
     */
    private val _roomCount = MutableLiveData(1)
    val roomCount: LiveData<Int> = _roomCount

    private val _adultCount = MutableLiveData(1)
    val adultCount: LiveData<Int> = _adultCount

    private val _childrenCount = MutableLiveData(0)
    val childrenCount: LiveData<Int> = _childrenCount

    fun updateRoomCount(count: Int) {
        _roomCount.value = count
    }

    fun updateAdultCount(count: Int) {
        _adultCount.value = count
    }

    fun updateChildrenCount(count: Int) {
        _childrenCount.value = count
    }

    fun getRoomCount(): String {
        return "${_adultCount.value} Người lớn, ${_roomCount.value} Phòng"
    }

    fun updateUser(userJWTPayloadModel: UserJWTPayloadModel) {
        userJWTPayloadModel.let {
            _userJWTPayload.value = it
        }
    }







    /**
     * Call API To GET My Booking
     *
     * This function GET My Booking
     * Get Paging Public Posting
     * @param pageNo
     * @param pageSize
     *
     * Check Add Loading More
     * Current Page Tracking
     */
    private val _myBooking = MutableLiveData<Resource<MyBookingResponse>>()
    val myBooking: MutableLiveData<Resource<MyBookingResponse>> get() = _myBooking
    fun getMyBooking(token: String, pageNo: Int, pageSize: Int) {
        viewModelScope.launch {
            _myBooking.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerBooking(token, pageNo, pageSize).let {
                _myBooking.postValue(it)
            }
        }
    }
    fun updateBookingItemById(bookingId: Int, newStatus: String) {
        // Tìm vị trí của item cần cập nhật trong danh sách
        val index = _currentMyBookingList.indexOfFirst { it.bookingId == bookingId }
        if (index != -1) {
            // Cập nhật trạng thái bookingStatus
            val updatedItem = _currentMyBookingList[index].copy(status = newStatus)
            _currentMyBookingList[index] = updatedItem
        }
    }

    // Check Current Posting Page
    private var _currentMyBookingPage = MutableLiveData<Int>()
    val currentMyBookingPage: MutableLiveData<Int>
        get() = _currentMyBookingPage
    fun incrementCurrentMyBookingPage() {
        val currentValue = _currentMyBookingPage.value ?: 0
        _currentMyBookingPage.value = currentValue + 1
    }

    private val _currentMyBookingList = mutableListOf<MyBookingResponse.Content>()
    fun loadMoreBookingList(list: List<MyBookingResponse.Content>) {
        _currentMyBookingList.addAll(list)
    }
    fun getCurrentMyBookingList(): List<MyBookingResponse.Content> {
        return _currentMyBookingList
    }

    /**
     * Call API To POST FeedBack
     *
     */
    private var _feedbackResponse = MutableLiveData<Resource<FeedbackResponse>>()
    val feedbackResponse: LiveData<Resource<FeedbackResponse>> = _feedbackResponse
    fun postFeedback(token: String, feedbackDTO: FeedbackDTO) {
        viewModelScope.launch {
            _feedbackResponse.postValue(Resource.loading(null))
            customerAPIRepository.postFeedbackForCustomerRental(token, feedbackDTO).let {
                _feedbackResponse.postValue(it)
            }
        }
    }


    fun clearCurrentMyBookingList() {
        _currentMyBookingList.clear()
        _currentMyBookingPage.value = 0
    }


    fun resetCurrentMyBookingPage() {
        _location.value = "Thành Phố Hồ Chí Minh"
        _dateRange.value = "20/10/2021 - 25/10/2021"
        _roomCount.value = 1
        _adultCount.value = 1
    }


    /**
     * Call API Save FCM Token
     *
     */
    private val _saveFCMToken = MutableLiveData<Resource<Boolean>>()
    val saveFCMToken: LiveData<Resource<Boolean>> = _saveFCMToken
    fun saveFCMToken(token: String, saveTokenDTO: SaveTokenDTO) {
        viewModelScope.launch {
            _saveFCMToken.postValue(Resource.loading(null))
            authAPIRepository.saveFCMToken(token, saveTokenDTO).let {
                _saveFCMToken.postValue(it)
            }
        }
    }







    init {

    }


}