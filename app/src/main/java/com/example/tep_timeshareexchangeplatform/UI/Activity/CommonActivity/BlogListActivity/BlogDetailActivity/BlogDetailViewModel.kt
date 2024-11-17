package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity.BlogDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogDetailViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
): ViewModel() {

    // Get My Exchange Detail
    private val _blogDetail = MutableLiveData<Resource<BlogDetailResponse>>()
    val blogDetail: MutableLiveData<Resource<BlogDetailResponse>> = _blogDetail
    fun getBlogDetail(postingId: Int) {
        viewModelScope.launch {
            _blogDetail.postValue(Resource.loading(null))
            publicPostingAPIRepository.getBlogDetail(postingId).let {
                _blogDetail.postValue(it)
            }
        }

    }
}