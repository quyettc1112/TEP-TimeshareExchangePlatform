package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentResultViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    // Call Get New Available Balance
    private val _customerInfoResponse = MutableLiveData<Resource<CustomerInfoResponse>>()
    val customerInfoResponse: MutableLiveData<Resource<CustomerInfoResponse>> get() = _customerInfoResponse
    fun getCustomerInfo(token: String) {
        viewModelScope.launch {
            _customerInfoResponse.postValue(Resource.loading(null))
            customerAPIRepository.getIsCustomerExist(token).let {
                _customerInfoResponse.postValue(it)
            }
        }
    }

    // Call Get Customer Profile
    private val _customerProfileResponse = MutableLiveData<Resource<CustomerProfileResponse>>()
    val customerProfileResponse: MutableLiveData<Resource<CustomerProfileResponse>> get() = _customerProfileResponse
    fun getCustomerProfile(token: String) {
        viewModelScope.launch {
            _customerProfileResponse.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerProfile(token).let {
                _customerProfileResponse.postValue(it)
            }
        }
    }

}