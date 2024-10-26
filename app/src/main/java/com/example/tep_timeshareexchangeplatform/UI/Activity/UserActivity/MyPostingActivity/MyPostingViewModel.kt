package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MyPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostingViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call My Posting API
    private val _myPosting = MutableLiveData<Resource<List<PostingsResponse.Content>>>()
    val myPosting: MutableLiveData<Resource<List<PostingsResponse.Content>>>
        get() = _myPosting
    fun getMyPostingList(token: String) {
        viewModelScope.launch {
            _myPosting.postValue(Resource.loading(null))
            customerAPIRepository.getMyPostingList(token).let {
                _myPosting.postValue(it)
            }
        }
    }


    // Call My Posting Detail API
    private val _myPostingDetail = MutableLiveData<Resource<PostingDetailResponse>>()
    val myPostingDetail: MutableLiveData<Resource<PostingDetailResponse>>
        get() = _myPostingDetail
    fun getMyPostingDetail(token: String, postingId: Int) {
        viewModelScope.launch {
            _myPostingDetail.postValue(Resource.loading(null))
            customerAPIRepository.getMyPostingDetail(token, postingId).let {
                _myPostingDetail.postValue(it)
            }
        }
    }

}
