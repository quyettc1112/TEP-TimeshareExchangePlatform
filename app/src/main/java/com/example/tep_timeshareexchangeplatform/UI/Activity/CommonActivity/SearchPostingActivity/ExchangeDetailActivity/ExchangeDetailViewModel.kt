package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ExchangeDetailActivity

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


}