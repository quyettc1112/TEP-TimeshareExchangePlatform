package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.RequestExchangeActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestExchangeViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
) : ViewModel() {

    // Get Exchange Posting Detail By ID
    private val _exchangePostingDetail = MutableLiveData<Resource<ExchangeDetailResponse>>()
    val exchangePostingDetail: MutableLiveData<Resource<ExchangeDetailResponse>> =
        _exchangePostingDetail

    fun callGetExchangePostingDetail(postingId: Int) {
        viewModelScope.launch {
            _exchangePostingDetail.postValue(Resource.loading(null))
            publicPostingAPIRepository.getExchangePostingDetail(postingId).let {
                _exchangePostingDetail.postValue(it)
            }
        }
    }

}