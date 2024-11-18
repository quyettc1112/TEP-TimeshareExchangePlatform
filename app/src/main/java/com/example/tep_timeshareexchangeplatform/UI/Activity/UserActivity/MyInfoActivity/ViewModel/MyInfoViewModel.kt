package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.UserJWTPayloadModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    private val _customerInfo = MutableLiveData<Resource<CustomerInfoResponse>>()
    val customerInfo: MutableLiveData<Resource<CustomerInfoResponse>> = _customerInfo
    fun getCustomerInfo(token: String) {
        viewModelScope.launch {
            _customerInfo.value = Resource.loading(null)
            customerAPIRepository.getIsCustomerExist(token).let {
                _customerInfo.value = it
            }
        }
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


    // Get Customer Profile
    private val _customerProfile = MutableLiveData<Resource<CustomerProfileResponse>>()
    val customerProfile: LiveData<Resource<CustomerProfileResponse>> = _customerProfile
    fun getCustomerProfile(token: String) {
        viewModelScope.launch {
            _customerProfile.value = Resource.loading(null)
            customerAPIRepository.getCustomerProfile(token).let {
                _customerProfile.value = it
            }
        }
    }



}