package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.PostingOfResortListActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangesResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingOfResortViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
) : ViewModel() {

    private val _currentResortID = MutableLiveData<Int>()
    fun setCurrentResortID(resortID: Int) {
        _currentResortID.value = resortID
    }
    fun getCurrentResortID(): Int {
        return _currentResortID.value ?: 0
    }

    // Call Get Public Posting API
    private val _publicRentalPosingList = MutableLiveData<Resource<PublicPostingResponse>>()
    val publicRentalPosingList: MutableLiveData<Resource<PublicPostingResponse>> get() = _publicRentalPosingList
    fun getRentalPostingList(pageNo: Int, pageSize: Int) {
        viewModelScope.launch {
            _publicRentalPosingList.postValue(Resource.loading(null))
            publicPostingAPIRepository.getRentalPostingOfResortByID(getCurrentResortID(), pageNo, pageSize).let {
                _publicRentalPosingList.postValue(it)
            }
        }
    }

    private val _currentPostingList = MutableLiveData<List<PublicPostingResponse.Content>>()
    fun loadMorePostings(list: List<PublicPostingResponse.Content>) {
        val currentList = _currentPostingList.value ?: emptyList()
        val updatedList = currentList + list
        _currentPostingList.value = updatedList
    }

    fun getCurrentPostingList(): List<PublicPostingResponse.Content>? {
        return _currentPostingList.value
    }

    private val _currentPostingsPage = MutableLiveData<Int>()
    var currentPostingsPage: LiveData<Int> = _currentPostingsPage
    fun getCurrentPostingsPage(): Int {
        return _currentPostingsPage.value ?: 0
    }

    fun incrementCurrentPostingsPage() {
        val currentValue = _currentPostingsPage.value ?: 0
        _currentPostingsPage.value = currentValue + 1
    }

    // Call Get Public Exchange Posting API
    private val _publicExchangePosingList = MutableLiveData<Resource<ExchangesResponse>>()
    val publicExchangePosingList: MutableLiveData<Resource<ExchangesResponse>> get() = _publicExchangePosingList

    fun getExchangePostingList(pageNo: Int, pageSize: Int, name: String) {
        viewModelScope.launch {
            _publicExchangePosingList.postValue(Resource.loading(null))
            publicPostingAPIRepository.getExchangePostings(pageNo, pageSize, name).let {
                _publicExchangePosingList.postValue(it)
            }
        }
    }

    private val _currentExchangeList = MutableLiveData<List<ExchangesResponse.Content>>()
    fun loadMoreExchange(list: List<ExchangesResponse.Content>) {
        val currentList = _currentExchangeList.value ?: emptyList()
        val updatedList = currentList + list
        _currentExchangeList.value = updatedList
    }

    fun getCurrentExchangeList(): List<ExchangesResponse.Content>? {
        return _currentExchangeList.value
    }

    private val _currentExchangePage = MutableLiveData<Int>()
    var currentExchangePage: LiveData<Int> = _currentExchangePage
    fun getCurrentExchangePage(): Int {
        return _currentExchangePage.value ?: 0
    }

    fun incrementCurrentExchangePage() {
        val currentValue = _currentExchangePage.value ?: 0
        _currentExchangePage.value = currentValue + 1
    }


    init {
        _currentPostingsPage.value = 0
        _currentExchangePage.value = 0
    }
}