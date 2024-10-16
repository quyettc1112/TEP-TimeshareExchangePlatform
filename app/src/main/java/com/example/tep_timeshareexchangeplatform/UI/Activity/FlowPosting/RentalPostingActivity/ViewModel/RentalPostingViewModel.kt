package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.RoomAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Service.RoomAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.Resort.ResortModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RentalPostingViewModel @Inject constructor(
    private val roomAPIRepository: RoomAPIRepository
) : ViewModel() {

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



    // ----------------------------------------------------------//
    // Call List Room of Resort API Selected
    // Init MutableLiveData for resort list
    private val _roomList = MutableLiveData<Resource<List<RoomModel>>>()
    val roomList: MutableLiveData<Resource<List<RoomModel>>> = _roomList
    // Function to get resort list
    fun getRoomListByResortId(token: String, resortID: Int) {
        viewModelScope.launch {
            _roomList.postValue(Resource.loading(null))
            roomAPIRepository.getRoomListByResortId(token, resortID).let {
                _roomList.postValue(it)
            }
        }
    }






    // Init
    init {
        _step.value = initStep
        _currentStepInProgress.value = initStep
        _stepCreateTimeshare.value = 0
    }





}