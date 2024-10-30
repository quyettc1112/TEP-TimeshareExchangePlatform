package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostingDetailViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel(){

    // Call My posting detail
    private val _postingDetailResponse = MutableLiveData<Resource<MyPostingDetailResponse>>()
    val postingDetailResponse: MutableLiveData<Resource<MyPostingDetailResponse>>
        get() = _postingDetailResponse
    fun getMyPostingDetail(token: String, postingId: Int) {
        viewModelScope.launch {
            _postingDetailResponse.postValue(Resource.loading(null))
            customerAPIRepository.getMyPostingDetail(token, postingId).let {
                _postingDetailResponse.postValue(it)
            }
        }
    }
}