package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingListActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostingViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call My Posting Detail API
    private val _myPostingList = MutableLiveData<Resource<MyRentalPostingsResponse>>()
    val myPostingList: MutableLiveData<Resource<MyRentalPostingsResponse>>
        get() = _myPostingList
    fun getMyPostingList(token: String, page: Int, size: Int) {
        viewModelScope.launch {
            _myPostingList.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerPostingList(token, page, size).let {
                _myPostingList.postValue(it)
            }
        }
    }

    // Check Current Posting Page
    private var _currentPostingPage = MutableLiveData<Int>()
    val currentPostingPage: MutableLiveData<Int>
        get() = _currentPostingPage

    fun incrementCurrentPostingsPage() {
        val currentValue = _currentPostingPage.value ?: 0
        _currentPostingPage.value = currentValue + 1
    }

    private val _currentPostingList = mutableListOf<MyRentalPostingsResponse.Content>()
    fun loadMorePostingList(list: List<MyRentalPostingsResponse.Content>) {
        _currentPostingList.addAll(list)
    }
    fun updatePostingItem(postingId: Int, newStatus: String) {
        // Tìm vị trí của item trong danh sách hiện tại
        val index = _currentPostingList.indexOfFirst { it.rentalPostingId == postingId }

        if (index != -1) { // Nếu tìm thấy item
            // Cập nhật item với trạng thái mới
            val updatedItem = _currentPostingList[index].copy(status = newStatus)

            // Thay thế item trong danh sách
            _currentPostingList[index] = updatedItem
        }
    }

    fun getCurrentPostingList(): List<MyRentalPostingsResponse.Content> {
        return _currentPostingList
    }

    fun clearCurrentPostingList() {
        _currentPostingList.clear()
    }


    // Check Current Package Selection




    init {
        _currentPostingPage.value = 0

    }
    // Hide Posting Function
    private val _hidePostingResponse = MutableLiveData<Resource<MyRentalPostingDetailResponse>>()
    val deactivateRentalPosting: MutableLiveData<Resource<MyRentalPostingDetailResponse>> =
        _hidePostingResponse

    fun deActiveRentalPosting(token: String, postingId: Int) {
        viewModelScope.launch {
            _hidePostingResponse.postValue(Resource.loading(null))
            val response = customerAPIRepository.deactivateRentalPosting(token, postingId)
            _hidePostingResponse.postValue(response)
        }
    }


}
