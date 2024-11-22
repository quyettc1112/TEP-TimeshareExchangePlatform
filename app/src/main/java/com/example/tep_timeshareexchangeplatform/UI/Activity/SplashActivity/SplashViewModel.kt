package com.example.tep_timeshareexchangeplatform.UI.Activity.SplashActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    // Check if customer exist, customer Info
    private val _customerInfoResponse = MutableLiveData<Resource<CustomerInfoResponse>>()
    val customerInfoResponse: LiveData<Resource<CustomerInfoResponse>> get() = _customerInfoResponse
    // Call Check if customer exist, customer Info Function
    fun getIsCustomerExist(token: String) {
        viewModelScope.launch {
            _customerInfoResponse.postValue(Resource.loading(null))
            val result = customerAPIRepository.getIsCustomerExist(token)
            _customerInfoResponse.postValue(result)
        }
    }

    // Call Get User Profile
    private val _customerProfileResponse = MutableLiveData<Resource<CustomerProfileResponse>>()
    val customerProfileResponse: LiveData<Resource<CustomerProfileResponse>> get() = _customerProfileResponse
    fun getCustomerProfile(token: String) {
        viewModelScope.launch {
            _customerProfileResponse.postValue(Resource.loading(null))
            val result = customerAPIRepository.getCustomerProfile(token)
            _customerProfileResponse.postValue(result)
        }
    }
}