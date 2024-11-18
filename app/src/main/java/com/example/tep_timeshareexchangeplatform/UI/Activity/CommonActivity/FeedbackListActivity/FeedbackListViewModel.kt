package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.FeedbackListActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Feedback.FeedbacksResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackListViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
) : ViewModel() {

    // List Feedback Respose
    private val _feedBackResponse = MutableLiveData<Resource<FeedbacksResponse>>()
    val feedBackResponse: MutableLiveData<Resource<FeedbacksResponse>>
        get() = _feedBackResponse

    // FUn To Call



    // Get Current Page
    private var _currentPage = MutableLiveData<Int>()
    val currentPage: MutableLiveData<Int>
        get() = _currentPage
    // Increment Current Page
    fun incrementCurrentPage() {
        val currentValue = _currentPage.value ?: 0
        _currentPage.value = currentValue + 1
    }

    // Get Current Posting List
    private val _currentFeedbackList = mutableListOf<FeedbacksResponse.Content>()
    fun loadMoreFeedbackList(list: List<FeedbacksResponse.Content>) {
        _currentFeedbackList.addAll(list)
    }
    fun getCurrentFeedbackList(): List<FeedbacksResponse.Content> {
        return _currentFeedbackList
    }

    fun clearCurrentFeedbackList() {
        _currentFeedbackList.clear()
    }

    init {
        _currentPage.value = 0
    }




}