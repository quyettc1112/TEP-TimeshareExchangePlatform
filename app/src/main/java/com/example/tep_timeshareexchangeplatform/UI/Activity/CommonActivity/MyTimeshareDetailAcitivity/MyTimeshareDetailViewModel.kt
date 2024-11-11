package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTimeshareDetailViewModel @Inject constructor(
    private val timeshareRepository: TimeshareRepository
): ViewModel() {

    // ----------------------------------------------------------//
    // Call API get my timeshare Detail
    private val _myTimeshareDetail = MutableLiveData<Resource<MyTimeshareDetailResponse>>()
    val myTimeshareDetail: MutableLiveData<Resource<MyTimeshareDetailResponse>> = _myTimeshareDetail
    fun getMyTimeshareDetail(token: String, timeshareID: Int) {
        viewModelScope.launch {
            _myTimeshareDetail.postValue(Resource.loading(null))
            timeshareRepository.getMyTimeshareDetail(token, timeshareID).let {
                _myTimeshareDetail.postValue(it)
            }
        }
    }
    // ----------------------------------------------------------//
    // Map Timeshare Detail to MyTimeshareResponse
    private val _myTimeshareResponse = MutableLiveData<MyTimeshareResponse.Content>()
    val myTimeshareResponse: MutableLiveData<MyTimeshareResponse.Content> = _myTimeshareResponse
    fun mapTimeshareDetailToMyTimeshareResponse(timeshareDetail: MyTimeshareDetailResponse) {
        val myTimeshareResponse = MyTimeshareResponse.Content(
            timeShareId = timeshareDetail.timeShareId,
            resortName = timeshareDetail.resortName,
            roomName = timeshareDetail.roomName,
            bathRoom = timeshareDetail.unitType.bathrooms,
            bedRooms = timeshareDetail.unitType.bedrooms,
            startDate = timeshareDetail.startDate,
            endDate = timeshareDetail.endDate
        )
        _myTimeshareResponse.postValue(myTimeshareResponse)
    }

}