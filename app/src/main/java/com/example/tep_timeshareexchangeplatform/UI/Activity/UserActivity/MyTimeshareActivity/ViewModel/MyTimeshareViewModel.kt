package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTimeshareViewModel @Inject constructor(
    private val timeshareRepository: TimeshareRepository,
): ViewModel() {

    private val _typeCallMyTimeshareList = MutableLiveData<Boolean>()
    val typeCallMyTimeshareList: MutableLiveData<Boolean> = _typeCallMyTimeshareList
    fun setTypeCallMyTimeshareList(type: Boolean) {
        _typeCallMyTimeshareList.value = type
    }

    fun getTypeCallMyTimeshareList(): Boolean {
        return _typeCallMyTimeshareList.value ?: false
    }

    private val _exchangePostingId = MutableLiveData<Int>()
    val exchangePostingId: MutableLiveData<Int> = _exchangePostingId
    fun setExchangePostingId(id: Int) {
        _exchangePostingId.value = id
    }


    // Call API get my timeshare list
    private val _myTimeshareList = MutableLiveData<Resource<MyTimeshareResponse>>()
    val myTimeshareList: MutableLiveData<Resource<MyTimeshareResponse>> = _myTimeshareList
    fun getMyTimeshareList(token: String, page: Int, size: Int) {
        viewModelScope.launch {
            _myTimeshareList.postValue(Resource.loading(null))
            if (!getTypeCallMyTimeshareList()) {
                timeshareRepository.getMyTimeshareList(token, page, size).let {
                    _myTimeshareList.postValue(it)
                }
            } else {
                timeshareRepository.getMyTimeshareValidExchange(token, page, size, exchangePostingId.value ?: 0).let {
                    _myTimeshareList.postValue(it)
                }
            }
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
    private val _currentMyTimeshareList = mutableListOf<MyTimeshareResponse.Content>()
    fun loadMoreMyTimeshareList(list: List<MyTimeshareResponse.Content>) {
        _currentMyTimeshareList.addAll(list)
    }
    fun getCurrentMyTimeshareList(): List<MyTimeshareResponse.Content> {
        return _currentMyTimeshareList
    }

    fun clearCurrentMyTimeshareList() {
        _currentMyTimeshareList.clear()
    }

    init {
        _typeCallMyTimeshareList.value = false
        _currentPage.value = 0
    }


}