package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ApproveExchangeResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
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
}