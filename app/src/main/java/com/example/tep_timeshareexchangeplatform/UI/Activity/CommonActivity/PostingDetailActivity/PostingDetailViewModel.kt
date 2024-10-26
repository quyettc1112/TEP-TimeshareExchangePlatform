package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingDetailViewModel @Inject constructor(
    private val postingAPIRepository: PostingAPIRepository
) :ViewModel() {

    // Call get Posting Detail API
    private val _postingDetail = MutableLiveData<Resource<PostingDetailResponse>>()
    val postingDetail: MutableLiveData<Resource<PostingDetailResponse>> = _postingDetail
    fun getPostingDetail(postingId: Int) {
        viewModelScope.launch {
            _postingDetail.postValue(Resource.loading(null))
            postingAPIRepository.getPostingDetail(postingId).let {
                _postingDetail.postValue(it)
            }
        }
    }

}