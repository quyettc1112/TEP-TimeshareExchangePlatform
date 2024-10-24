package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.TimeshareFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeshareViewModel @Inject constructor(
    private val postingAPIRepository: PostingAPIRepository,
) : ViewModel() {
    // Call API Postings
    private val _postingsResponse = MutableLiveData<Resource<PostingsResponse>>()
    val postingsResponse: MutableLiveData<Resource<PostingsResponse>> get() = _postingsResponse
    fun getPostings(pageNo: Int, pageSize: Int, resortName: String)  {
        viewModelScope.launch {
            _postingsResponse.postValue(Resource.loading(null))
            postingAPIRepository.getPostings(pageNo, pageSize, resortName).let {
                _postingsResponse.postValue(it)
            }
        }
    }
}