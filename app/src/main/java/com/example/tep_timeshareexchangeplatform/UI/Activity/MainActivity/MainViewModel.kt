package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.AuthAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.UserJWTPayloadModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authAPIRepository: AuthAPIRepository,
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val publicResortAPIRepository: PublicResortAPIRepository
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
    private val _customerInfo = MutableLiveData<CustomerInfoResponse>()
    val customerInfo: LiveData<CustomerInfoResponse> = _customerInfo
    fun setCustomerInfo(customerInfoResponse: CustomerInfoResponse) {
        _customerInfo.value = customerInfoResponse
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
     * Call API To GET Public Posting and Resort
     *
     * This function is responsible for Public Posting API For Top Resort.
     * Get Paging Public Posting
     * @param pageNo
     * @param pageSize
     * @param resortName
     * @return
     *
     * Check Add Loading More
     * Current Page Tracking
     */
    // Call API ALL Postings In Top Resort Fragment
    private val _posting_TopResort = MutableLiveData<Resource<PublicPostingResponse>>()
    val posting_TopResort: MutableLiveData<Resource<PublicPostingResponse>> get() = _posting_TopResort
    fun getPostingOnTopResort(pageNo: Int, pageSize: Int, resortName: String) {
        viewModelScope.launch {
            _posting_TopResort.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, resortName).let {
                _posting_TopResort.postValue(it)
            }
        }
    }
    private val _currentPostingList = MutableLiveData<List<PublicPostingResponse.Content>>()
    fun loadMorePostings(list: List<PublicPostingResponse.Content>) {
        val currentList = _currentPostingList.value ?: emptyList()
        val updatedList = currentList + list
        _currentPostingList.value = updatedList
    }
    fun getCurrentPostingList(): List<PublicPostingResponse.Content>? {
        return _currentPostingList.value
    }

    private val _currentPostingsPage = MutableLiveData<Int>()
    var currentPostingsPage: LiveData<Int> = _currentPostingsPage
    fun getCurrentPostingsPage(): Int {
        return _currentPostingsPage.value ?: 0
    }
    fun incrementCurrentPostingsPage() {
        val currentValue = _currentPostingsPage.value ?: 0
        _currentPostingsPage.value = currentValue + 1
    }



    // Call API ALL Resort In Top Resort Fragment
    private val _resort_TopResort = MutableLiveData<Resource<ResortModelResponse>>()
    val resort_TopResort: MutableLiveData<Resource<ResortModelResponse>> get() = _resort_TopResort
    fun getResortONTopResort(pageNo: Int, pageSize: Int, resortName: String) {
        viewModelScope.launch {
            _resort_TopResort.postValue(Resource.loading(null))
            publicResortAPIRepository.getResortList(pageNo, pageSize, resortName).let {
                _resort_TopResort.postValue(it)
            }
        }
    }
    private val _currentResortList = MutableLiveData<List<ResortModelResponse.Content>>()
    fun loadMoreResorts(list: List<ResortModelResponse.Content>) {
        val currentList = _currentResortList.value ?: emptyList()
        val updatedList = currentList + list
        _currentResortList.value = updatedList
    }
    fun getCurrentResortList(): List<ResortModelResponse.Content>? {
        return _currentResortList.value
    }

    private val _currentResortPage = MutableLiveData<Int>()
    var currentResortPage: LiveData<Int> = _currentPostingsPage
    fun getCurrentResortPage(): Int {
        return _currentResortPage.value ?: 0
    }
    fun incrementCurrentResortPage() {
        val currentValue = _currentPostingsPage.value ?: 0
        _currentPostingsPage.value = currentValue + 1
    }

    init {

    }


}