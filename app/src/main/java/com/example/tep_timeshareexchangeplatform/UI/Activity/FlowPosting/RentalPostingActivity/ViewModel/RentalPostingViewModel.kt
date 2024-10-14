package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.LocationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel

class RentalPostingViewModel: ViewModel() {

    private val initStep: Int = 1

    // Tracking Progress Step
    private val _step = MutableLiveData<Int>()
    val step: MutableLiveData<Int>
        get() = _step

    // Tracking Current Step In Progress
    private val _currentStepInProgress = MutableLiveData<Int>()
    val currentStepInProgress: LiveData<Int> get() = _currentStepInProgress

    // Tracking Date Selected
    // LiveData to hold the pair of start and end dates
    private val _dateRange = MutableLiveData<Pair<Long?, Long?>>()
    val dateRange: LiveData<Pair<Long?, Long?>> get() = _dateRange


    // Tracking Location Selected
    private val _locationModel = MutableLiveData<LocationModel>()
    val locationModel: MutableLiveData<LocationModel>
        get() = _locationModel


    // Tracking MyTimeshareModel Selected
    private val _myTimeshareModel = MutableLiveData<MyTimeshareModel>()
    val myTimeshareModelSelected: MutableLiveData<MyTimeshareModel>
        get() = _myTimeshareModel

    // Tracking Package Step 4 Selected
    private val _packageStep4 = MutableLiveData<PackageModel>()
    val packageStep4: MutableLiveData<PackageModel>
        get() = _packageStep4


    // Init
    init {
        _step.value = initStep
        _currentStepInProgress.value = initStep
    }


    // Funtion to update step
    fun updateStep(step: Int){
        if (step >= _currentStepInProgress.value!!){
            updateCurrentStepInProgress(step)
        }

        _step.value = step
    }
    fun updateCurrentStepInProgress(step: Int){
        _currentStepInProgress.value = step
    }

    // Function to check if a step can be navigated to
    fun canNavigateToStep(step: Int): Boolean {
        return _currentStepInProgress.value?.let { step <= it } ?: false
    }

    // Function to reset the current step
    fun resetSteps() {
        _currentStepInProgress.value = 1
    }


    fun updateLocationModel(locationModel: LocationModel){
        _locationModel.value = locationModel
    }


    // Funtion to update myTimeshareModel
    fun updateMyTimeshareModel(myTimeshareModel: MyTimeshareModel){
        _myTimeshareModel.value = myTimeshareModel
    }

    // Funtion to update packageStep4
    fun updatePackageStep4(packageModel: PackageModel){
        _packageStep4.value = packageModel
    }

    // Function to save the start and end dates
    fun setDateRange(startDate: Long?, endDate: Long?) {
        _dateRange.value = Pair(startDate, endDate)
    }

}