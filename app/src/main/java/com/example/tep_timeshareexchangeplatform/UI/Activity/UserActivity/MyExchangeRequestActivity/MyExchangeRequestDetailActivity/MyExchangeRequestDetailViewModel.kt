package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ApproveExchangeResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ExchangePriceValuationRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.RejectRequestRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyExchangeRequestDetailViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {
    // Get My Exchange Detail

    private val _myExchangeRequestDetail = MutableLiveData<Resource<MyExchangeRequestDetailResponse>>()
    val myExchangeRequestDetail: MutableLiveData<Resource<MyExchangeRequestDetailResponse>> = _myExchangeRequestDetail
    fun getCustomerExchangeDetail(token: String, id: Int) {
        viewModelScope.launch {
            _myExchangeRequestDetail.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerExchangeRequestDetail(token, id).let {
                _myExchangeRequestDetail.postValue(it)
            }
        }
    }

    // Approve Exchange Request
    private val _approveExchangeRequest = MutableLiveData<Resource<ApproveExchangeResponse>>()
    val approveExchangeRequest: MutableLiveData<Resource<ApproveExchangeResponse>> = _approveExchangeRequest
    fun approveExchangeRequest(token: String, id: Int) {
        viewModelScope.launch {
            _approveExchangeRequest.postValue(Resource.loading(null))
            customerAPIRepository.approveExchangeRequest(token, id).let {
                _approveExchangeRequest.postValue(it)
            }
        }
    }

    // Call Reject Exchange Request
    private val _rejectExchangeRequest = MutableLiveData<Resource<RejectRequestRespone>>()
    val rejectExchangeRequest: MutableLiveData<Resource<RejectRequestRespone>> = _rejectExchangeRequest
    fun rejectExchangeRequest(token: String, requestId: Int) {
        viewModelScope.launch {
            _rejectExchangeRequest.postValue(Resource.loading(null))
            customerAPIRepository.rejectExchangeRequest(token, requestId).let {
                _rejectExchangeRequest.postValue(it)
            }
        }
    }


    private val _exchangePriceValuation = MutableLiveData<Resource<ExchangePriceValuationRespone>>()
    val exchangePriceValuation: MutableLiveData<Resource<ExchangePriceValuationRespone>> = _exchangePriceValuation
    fun exchangePriceValuation(token: String, requestId: Int, priceValuatin: Long, note: String) {
        viewModelScope.launch {
            _exchangePriceValuation.postValue(Resource.loading(null))
            customerAPIRepository.exchangePriceValuation(token, requestId, priceValuatin, note).let {
                _exchangePriceValuation.postValue(it)
            }
        }
    }

    private val _price = MutableLiveData<Long>()
    val price: MutableLiveData<Long>
        get() = _price

    fun updatePrice(price: Long) {
        _price.value = price
    }
    init {
        _price.value = 0
    }



}