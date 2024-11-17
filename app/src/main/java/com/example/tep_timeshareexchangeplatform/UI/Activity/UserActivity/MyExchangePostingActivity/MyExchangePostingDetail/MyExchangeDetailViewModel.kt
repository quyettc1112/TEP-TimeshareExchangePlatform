package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyExchangeDetailViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    // Get My Exchange Detail
    private val _myExchangeDetail = MutableLiveData<Resource<MyExchangePostingDetailResponse>>()
    val myExchangeDetail: MutableLiveData<Resource<MyExchangePostingDetailResponse>> = _myExchangeDetail
    fun getCustomerExchangeDetail(token: String, id: Int) {
        viewModelScope.launch {
            _myExchangeDetail.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerExchangePostingDetail(token, id).let {
                _myExchangeDetail.postValue(it)
            }
        }

    }
}