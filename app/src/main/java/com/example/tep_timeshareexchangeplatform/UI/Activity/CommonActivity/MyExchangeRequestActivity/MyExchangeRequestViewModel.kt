package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyExchangeRequestActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyExchangeRequestViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {
    // Call Get ALl Exchange Request
    private val _myExchangeRequestList = MutableLiveData<Resource<MyExchangeRequestResponse>>()
    val myExchangeRequestList: MutableLiveData<Resource<MyExchangeRequestResponse>> =
        _myExchangeRequestList

    fun getMyExchangeRequestList(token: String, page: Int, size: Int) {
        viewModelScope.launch {
            _myExchangeRequestList.postValue(Resource.loading(null))
            val response =
                customerAPIRepository.getCustomerExchangeRequest(token, page, size)
            _myExchangeRequestList.postValue(response)
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
    private val _currentRequestList = mutableListOf<MyExchangeRequestResponse.Content>()
    fun loadMoreRequestList(list: List<MyExchangeRequestResponse.Content>) {
        _currentRequestList.addAll(list)
    }
    fun getCurrentRequestList(): List<MyExchangeRequestResponse.Content> {
        return _currentRequestList
    }

    fun clearCurrentRequestList() {
        _currentRequestList.clear()
    }

    init {
        _currentPage.value = 0
    }
}