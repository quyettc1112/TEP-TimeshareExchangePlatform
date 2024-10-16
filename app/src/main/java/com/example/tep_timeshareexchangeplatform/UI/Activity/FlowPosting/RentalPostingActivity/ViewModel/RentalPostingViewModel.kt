package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.ResortModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel

class RentalPostingViewModel: ViewModel() {

    private val initStep: Int = 1


    // ----------------------------------------------------------//
    // Tracking Progress Step
    private val _step = MutableLiveData<Int>()
    val step: MutableLiveData<Int>
        get() = _step
    fun updateStep(step: Int){
        if (step >= _currentStepInProgress.value!!){
            updateCurrentStepInProgress(step)
        }
        _step.value = step
    }



    // ----------------------------------------------------------//
    // Tracking Step in Step 2 (My Timeshare) - Create Timeshare
    private val _stepCreateTimeshare = MutableLiveData<Int>()
    val stepCreateTimeshare: MutableLiveData<Int>
        get() = _stepCreateTimeshare
    // Update the current step progress
    fun updateTaskProgress(currentTask: Int) {
        if (currentTask in 0..5) { // Assuming 5 tasks
            _stepCreateTimeshare.value = currentTask
        }
    }



    // ----------------------------------------------------------//
    // Tracking Current Step In Progress
    private val _currentStepInProgress = MutableLiveData<Int>()
    val currentStepInProgress: LiveData<Int> get() = _currentStepInProgress
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



    // ----------------------------------------------------------//
    // Tracking Date Selected
    // LiveData to hold the pair of start and end dates
    private val _dateRange = MutableLiveData<Pair<Long?, Long?>>()
    val dateRange: LiveData<Pair<Long?, Long?>> get() = _dateRange
    // Function to save the start and end dates
    fun setDateRange(startDate: Long?, endDate: Long?) {
        _dateRange.value = Pair(startDate, endDate)
    }



    // ----------------------------------------------------------//
    // Tracking Location Selected
    private val _resortModel = MutableLiveData<ResortModel.Content>()
    val resortModel: MutableLiveData<ResortModel.Content>
        get() = _resortModel
    fun updateResortModel(resortModel: ResortModel.Content){
        _resortModel.value = resortModel
    }


    // ----------------------------------------------------------//
    // Tracking MyTimeshareModel Selected
    private val _myTimeshareModel = MutableLiveData<MyTimeshareModel>()
    val myTimeshareModelSelected: MutableLiveData<MyTimeshareModel>
        get() = _myTimeshareModel
    // Funtion to update myTimeshareModel
    fun updateMyTimeshareModel(myTimeshareModel: MyTimeshareModel){
        _myTimeshareModel.value = myTimeshareModel
    }


    // ----------------------------------------------------------//
    // Tracking Package Step 4 Selected
    private val _packageStep4 = MutableLiveData<PackageModel>()
    val packageStep4: MutableLiveData<PackageModel>
        get() = _packageStep4
    // Funtion to update packageStep4
    fun updatePackageStep4(packageModel: PackageModel){
        _packageStep4.value = packageModel
    }

    // Init
    init {
        _step.value = initStep
        _currentStepInProgress.value = initStep
        _stepCreateTimeshare.value = 0
    }





}