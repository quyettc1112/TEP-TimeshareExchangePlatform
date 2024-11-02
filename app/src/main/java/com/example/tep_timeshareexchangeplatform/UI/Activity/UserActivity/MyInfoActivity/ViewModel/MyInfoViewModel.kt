package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
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
}