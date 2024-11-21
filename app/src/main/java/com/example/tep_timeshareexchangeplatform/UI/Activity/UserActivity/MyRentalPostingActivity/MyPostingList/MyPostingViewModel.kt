package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingList

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
    private val _hidePostingResponse = MutableLiveData<Resource<MyRentalPostingsResponse>>()
    val deactivateRentalPosting: MutableLiveData<Resource<MyRentalPostingsResponse>> =
        _hidePostingResponse
    fun deActiveRentalPosting(token: String, postingId: Int) {
        viewModelScope.launch {
            _hidePostingResponse.postValue(Resource.loading(null))
            val response = customerAPIRepository.deactivateRentalPosting(token, postingId)
            _hidePostingResponse.postValue(response)
        }
    }

}
