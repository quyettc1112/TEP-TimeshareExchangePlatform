package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingDetailViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
) :ViewModel() {

    // Call get Posting Detail API
    private val _postingDetail = MutableLiveData<Resource<PublicPostingDetailResponse>>()
    val postingDetail: MutableLiveData<Resource<PublicPostingDetailResponse>> = _postingDetail
    fun getPostingDetail(postingId: Int) {
        viewModelScope.launch {
            _postingDetail.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostingDetail(postingId).let {
                _postingDetail.postValue(it)
            }
        }
    }

}