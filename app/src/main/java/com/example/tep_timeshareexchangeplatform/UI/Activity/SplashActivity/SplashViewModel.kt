package com.example.tep_timeshareexchangeplatform.UI.Activity.SplashActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    /*// Check Customer Exist
    private val _customerResponse = MutableLiveData<Resource<CustomerResponse>>()
    val customerResponse: MutableLiveData<Resource<CustomerResponse>> get() = _customerResponse
    fun getIsCustomerExist(token: String, userId: Int) {
        viewModelScope.launch {
            _customerResponse.postValue(Resource.loading(null))
            customerAPIRepository.getIsCustomerExist(token, userId).let {
                _customerResponse.postValue(it)
            }
        }
    }*/
}