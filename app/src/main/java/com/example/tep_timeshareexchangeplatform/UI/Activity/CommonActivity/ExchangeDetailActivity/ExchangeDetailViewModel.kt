package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ExchangeDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeDetailViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    // Call Get Exchange Detail API
    private val _exchangeDetail = MutableLiveData<Resource<ExchangeDetailResponse>>()
    val exchangeDetail: MutableLiveData<Resource<ExchangeDetailResponse>> = _exchangeDetail
    fun getExchangeDetail(exchangeId: Int) {
        viewModelScope.launch {
            _exchangeDetail.postValue(Resource.loading(null))
            publicPostingAPIRepository.getExchangePostingDetail(exchangeId).let {
                _exchangeDetail.postValue(it)
            }
        }
    }


    // Call API Get Is Customer Exist
    private val _isCustomerExist = MutableLiveData<Resource<CustomerInfoResponse>>()
    val isCustomerExist: MutableLiveData<Resource<CustomerInfoResponse>>
        get() = _isCustomerExist
    fun callIsCustomerExist(token: String) {
        viewModelScope.launch {
            _isCustomerExist.postValue(Resource.loading(null))
            customerAPIRepository.getIsCustomerExist(token).let {
                _isCustomerExist.postValue(it)
            }
        }
    }

    // Call API Create Customer
    private val _customerResponse = MutableLiveData<Resource<CustomerResponse>>()
    val createCustomerResponse: MutableLiveData<Resource<CustomerResponse>>
        get() = _customerResponse
    fun callCreateCustomer(token: String, customerDTO: CustomerDTO) {
        viewModelScope.launch {
            _customerResponse.postValue(Resource.loading(null))
            customerAPIRepository.createCustomer(token, customerDTO).let {
                _customerResponse.postValue(it)
            }
        }
    }
}