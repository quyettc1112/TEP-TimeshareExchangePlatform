package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.ExchangeRequestPostingResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeRequestOnPostViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {
    // Call Get ALl Exchange Request
    private val _myExchangeRequestOnPostList = MutableLiveData<Resource<ExchangeRequestPostingResponse>>()
    val myExchangeRequestOnPostList: MutableLiveData<Resource<ExchangeRequestPostingResponse>> =
        _myExchangeRequestOnPostList

    fun getMyExchangeRequestOnPostList(token: String, pageNo: Int, pageSize: Int, postingId: Int) {
        viewModelScope.launch {
            _myExchangeRequestOnPostList.postValue(Resource.loading(null))
            val response =
                customerAPIRepository.getCustomerExchangeRequestPost(token, pageNo, pageSize, postingId)
            _myExchangeRequestOnPostList.postValue(response)
        }
    }

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
    private val _currentRequestOnPostList = mutableListOf<ExchangeRequestPostingResponse.Content>()
    fun loadMoreRequestOnPostList(list: List<ExchangeRequestPostingResponse.Content>) {
        _currentRequestOnPostList.addAll(list)
    }
    fun getCurrentRequestOnPostList(): List<ExchangeRequestPostingResponse.Content> {
        return _currentRequestOnPostList
    }

    fun clearCurrentRequestOnPostList() {
        _currentRequestOnPostList.clear()
    }

    init {
        _currentPage.value = 0
    }
}