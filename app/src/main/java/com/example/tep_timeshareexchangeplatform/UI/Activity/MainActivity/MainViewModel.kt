package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.AuthAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.ResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PostingsResponse
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
    private val resortAPIRepository: ResortAPIRepository
) : ViewModel() {

    private val initStep: Int = 1


    // ----------------------------------------------------------//
    // Tracking Progress Step
    private val _step = MutableLiveData<Int>()
    val step: MutableLiveData<Int>
        get() = _step

    fun updateStep(step: Int) {
        if (step >= _currentStepInProgress.value!!) {
            updateCurrentStepInProgress(step)
        }
        _step.value = step
    }

    // Tracking Current Step In Progress
    private val _currentStepInProgress = MutableLiveData<Int>()
    val currentStepInProgress: LiveData<Int> get() = _currentStepInProgress
    fun updateCurrentStepInProgress(step: Int) {
        _currentStepInProgress.value = step
    }

    // Function to check if a step can be navigated to
    fun canNavigateToStep(step: Int): Boolean {
        return _currentStepInProgress.value?.let { step <= it } ?: false
    }

    // Function to reset the current step
    fun resetSteps() {
        _currentStepInProgress.value = 1
    }


    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location
    fun updateLocation(location: String) {
        _location.value = location
    }


    private val _userJWTPayload = MutableLiveData<UserJWTPayloadModel>()
    val userJWTPayload: LiveData<UserJWTPayloadModel> = _userJWTPayload

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
        return "${_adultCount.value} Người lớn, ${_childrenCount.value} Trẻ em, ${_roomCount.value} Phòng"
    }

    fun updateUser(userJWTPayloadModel: UserJWTPayloadModel) {
        userJWTPayloadModel.let {
            _userJWTPayload.value = it
        }
    }

    // Call API Postings
    private val _postingsResponse = MutableLiveData<Resource<PublicPostingResponse>>()
    val postingsResponse: MutableLiveData<Resource<PublicPostingResponse>> get() = _postingsResponse
    fun getPostings(pageNo: Int, pageSize: Int, resortName: String) {
        viewModelScope.launch {
            _postingsResponse.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, resortName).let {
                _postingsResponse.postValue(it)
            }
        }
    }


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

    // Call Public All Posting
    private val _publicPostingsResponse = MutableLiveData<Resource<PublicPostingResponse>>()
    val publicPostingsResponseHome: MutableLiveData<Resource<PublicPostingResponse>> get() = _publicPostingsResponse
    fun getPublicPostingsHome(pageNo: Int, pageSize: Int, resortName: String) {
        viewModelScope.launch {
            _publicPostingsResponse.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, resortName).let {
                _publicPostingsResponse.postValue(it)
            }
        }
    }

    // Call API ALL Postings In Top Resort Fragment
    private val _postingsResponseTopResort = MutableLiveData<Resource<PublicPostingResponse>>()
    val postingsResponseTopResort: MutableLiveData<Resource<PublicPostingResponse>> get() = _postingsResponseTopResort
    fun getPostingsTopResort(pageNo: Int, pageSize: Int, resortName: String) {
        viewModelScope.launch {
            _postingsResponseTopResort.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, resortName).let {
                _postingsResponseTopResort.postValue(it)
            }
        }
    }

    // Call API ALL Resort In Top Resort Fragment
    private val _resortResponseTopResort = MutableLiveData<Resource<ResortModelResponse>>()
    val resortResponseOnTopResort: MutableLiveData<Resource<ResortModelResponse>> get() = _resortResponseTopResort
    fun getResortONTopResort(pageNo: Int, pageSize: Int, resortName: String) {
        viewModelScope.launch {
            _resortResponseTopResort.postValue(Resource.loading(null))
            resortAPIRepository.getResortList(pageNo, pageSize, resortName).let {
                _resortResponseTopResort.postValue(it)
            }
        }
    }


}