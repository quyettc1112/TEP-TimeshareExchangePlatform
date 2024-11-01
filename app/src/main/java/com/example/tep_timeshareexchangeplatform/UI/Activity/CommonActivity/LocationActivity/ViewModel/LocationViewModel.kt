package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel@Inject constructor(
    private val publicResortAPIRepository: PublicResortAPIRepository
) : ViewModel() {

    // Init MutableLiveData for resort list
    private val _resortList = MutableLiveData<Resource<ResortModelResponse>>()
    val resortList: MutableLiveData<Resource<ResortModelResponse>> = _resortList

    // Init MutableLiveData for resort name
    private val _resortName = MutableLiveData<String>()
    val resortName: MutableLiveData<String> = _resortName

    // Function to get resort list
    fun getResortList(pageNo: Int, pageSize: Int, resortName: String?) {
        viewModelScope.launch {
            _resortList.postValue(Resource.loading(null))
            publicResortAPIRepository.getResortList(pageNo, pageSize, resortName).let {
                _resortList.postValue(it)
            }
        }
    }
    // Function to set resort name
    fun setResortName(name: String) {
        _resortName.value = name
    }



}