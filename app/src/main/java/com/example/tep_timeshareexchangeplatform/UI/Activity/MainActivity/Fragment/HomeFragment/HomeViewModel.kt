package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.HomeFragment

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
class HomeViewModel @Inject constructor(
    private val publicResortAPIRepository: PublicResortAPIRepository,
    private val publicPostingAPIRepository: PublicPostingAPIRepository
) : ViewModel() {

    // Init MutableLiveData for resort list
    private val _resortList = MutableLiveData<Resource<ResortModelResponse>>()
    val resortList: MutableLiveData<Resource<ResortModelResponse>> = _resortList

    // Function to get resort list
    fun getResortList(pageNo: Int, pageSize: Int, resortName: String?) {
        viewModelScope.launch {
            _resortList.postValue(Resource.loading(null))
            publicResortAPIRepository.getResortList(pageNo, pageSize, resortName).let {
                _resortList.postValue(it)
            }
        }
    }

    /**
     * Tracking Call API Public Posting in Home Fragment
     *
     * This function is responsible for Public Posting API For Home.
     */
    // Call Public All Posting FOR HOME Fragment
    private val _home_postingList = MutableLiveData<Resource<PublicPostingResponse>>()
    val home_PostingList: MutableLiveData<Resource<PublicPostingResponse>> get() = _home_postingList
    fun getPublicPostingsHome(pageNo: Int, pageSize: Int, resortName: String) {
        viewModelScope.launch {
            _home_postingList.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, resortName).let {
                _home_postingList.postValue(it)
            }
        }
    }


}