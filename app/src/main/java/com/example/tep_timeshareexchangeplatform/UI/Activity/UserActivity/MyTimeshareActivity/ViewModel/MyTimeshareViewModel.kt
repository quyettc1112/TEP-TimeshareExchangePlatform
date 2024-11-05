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
    // Call API get my timeshare list
    private val _myTimeshareList = MutableLiveData<Resource<MyTimeshareResponse>>()
    val myTimeshareList: MutableLiveData<Resource<MyTimeshareResponse>> = _myTimeshareList
    fun getMyTimeshareList(token: String) {
        viewModelScope.launch {
            _myTimeshareList.postValue(Resource.loading(null))
            timeshareRepository.getMyTimeshareList(token).let {
                _myTimeshareList.postValue(it)
            }
        }
    }

}