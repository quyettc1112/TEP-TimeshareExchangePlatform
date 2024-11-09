package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopResortViewModel @Inject constructor(
    private val publicResortAPIRepository: PublicResortAPIRepository
) : ViewModel() {

    // Get Alll Resort List
    private val _resortList = MutableLiveData<Resource<ResortModelResponse>>()
    val resortList: MutableLiveData<Resource<ResortModelResponse>> = _resortList
    fun getResortList(pageNo: Int, pageSize: Int, resortName: String?) {
        viewModelScope.launch {
            _resortList.postValue(Resource.loading(null))
            publicResortAPIRepository.getResortList(pageNo, pageSize, resortName).let {
                _resortList.postValue(it)
            }
        }
    }

    private val _currentResortList = MutableLiveData<List<ResortModelResponse.Content>>()
    fun loadMoreResortList(list: List<ResortModelResponse.Content>) {
        val currentList = _currentResortList.value ?: emptyList()
        val updatedList = currentList + list
        _currentResortList.value = updatedList
    }

    fun getCurrentResortList(): List<ResortModelResponse.Content>? {
        return _currentResortList.value
    }

    private val _currentResortPage = MutableLiveData<Int>()
    var currentResortPage: LiveData<Int> = _currentResortPage
    fun getCurrentResortsPage(): Int {
        return _currentResortPage.value ?: 0
    }

    fun incrementCurrentResortsPage() {
        val currentValue = _currentResortPage.value ?: 0
        _currentResortPage.value = currentValue + 1
    }

    fun resetCurrentPostingPage() {
        _currentResortPage.value = 0
        _currentResortList.value = emptyList()
    }

    init {
        _currentResortPage.value = 0
    }

}