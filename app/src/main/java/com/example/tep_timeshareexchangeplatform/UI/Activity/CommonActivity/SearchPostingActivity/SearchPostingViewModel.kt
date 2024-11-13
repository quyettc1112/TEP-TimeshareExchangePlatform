package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPostingViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
): ViewModel() {

    private val _publicRentalPosingList = MutableLiveData<Resource<PublicPostingResponse>>()
    val publicRentalPosingList: MutableLiveData<Resource<PublicPostingResponse>> get() = _publicRentalPosingList
    fun getRentalPostingList(pageNo: Int, pageSize: Int, name: String) {
        viewModelScope.launch {
            _publicRentalPosingList.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, name).let {
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


    init {
        _currentPostingsPage.value = 0
    }
}