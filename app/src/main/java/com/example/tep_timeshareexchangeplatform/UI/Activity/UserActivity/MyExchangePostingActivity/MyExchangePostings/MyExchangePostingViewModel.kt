package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyExchangePostingViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {
    // Call Get ALl Exchange Posting
    private val _myExchangePostingList = MutableLiveData<Resource<MyExchangePostingsResponse>>()
    val myExchangePostingList: MutableLiveData<Resource<MyExchangePostingsResponse>> =
        _myExchangePostingList

    fun getMyExchangePostingList(token: String, page: Int, size: Int) {
        viewModelScope.launch {
            _myExchangePostingList.postValue(Resource.loading(null))
            val response =
                customerAPIRepository.getCustomerExchangePosting(token, page, size)
            _myExchangePostingList.postValue(response)
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
    private val _currentPostingList = mutableListOf<MyExchangePostingsResponse.Content>()
    fun loadMorePostingList(list: List<MyExchangePostingsResponse.Content>) {
        _currentPostingList.addAll(list)
    }

    fun updatePostingItem(postingId: Int, newStatus: String) {
        // Tìm vị trí của item trong danh sách hiện tại
        val index = _currentPostingList.indexOfFirst { it.exchangePostingId == postingId }

        if (index != -1) { // Nếu tìm thấy item
            // Cập nhật item với trạng thái mới
            val updatedItem = _currentPostingList[index].copy(status = newStatus)

            // Thay thế item trong danh sách
            _currentPostingList[index] = updatedItem
        }
    }

    fun getCurrentPostingList(): List<MyExchangePostingsResponse.Content> {
        return _currentPostingList
    }

    fun clearCurrentPostingList() {
        _currentPostingList.clear()
    }

    init {
        _currentPage.value = 0
    }


    // Hide Posting Function
    private val _hidePostingResponse = MutableLiveData<Resource<MyExchangePostingsResponse>>()
    val deactivateExchangePosting: MutableLiveData<Resource<MyExchangePostingsResponse>> =
        _hidePostingResponse
    fun deActiveExchangePosting(token: String, postingId: Int) {
        viewModelScope.launch {
            _hidePostingResponse.postValue(Resource.loading(null))
            val response = customerAPIRepository.deactivateExchangePosting(token, postingId)
            _hidePostingResponse.postValue(response)
        }
    }


}