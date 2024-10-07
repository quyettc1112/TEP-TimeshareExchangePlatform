package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.MyTimeshareModel

class RentalPostingViewModel: ViewModel() {
    // Khai Bao
    private val _step = MutableLiveData<Int>()
    private val _locationModel = MutableLiveData<LocationModel>()
    private val _myTimeshareModel = MutableLiveData<MyTimeshareModel>()


    // Getter
    val step: MutableLiveData<Int>
        get() = _step

    val locationModel: MutableLiveData<LocationModel>
        get() = _locationModel

    val myTimeshareModelSelected: MutableLiveData<MyTimeshareModel>
        get() = _myTimeshareModel

    // Init
    init {
        _step.value = 1
    }


    // Funtion to update step
    fun updateStep(step: Int){
        _step.value = step
    }
    fun updateLocationModel(locationModel: LocationModel){
        _locationModel.value = locationModel
    }


    // Funtion to update myTimeshareModel
    fun updateMyTimeshareModel(myTimeshareModel: MyTimeshareModel){
        _myTimeshareModel.value = myTimeshareModel
    }


}