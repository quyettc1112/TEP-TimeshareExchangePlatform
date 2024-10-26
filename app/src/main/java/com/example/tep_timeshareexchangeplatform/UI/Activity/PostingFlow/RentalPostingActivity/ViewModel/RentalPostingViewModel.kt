package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.ResortAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.RoomAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.PostRoomRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.PostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RentalPostingViewModel @Inject constructor(
    private val roomAPIRepository: RoomAPIRepository,
    private val resortAPIRepository: ResortAPIRepository,
    private val timeshareRepository: TimeshareRepository
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
    fun resetDateRange() {
        _dateRange.value = Pair(null, null)
    }
    fun getNumberOfNights(): Int {
        val range = _dateRange.value
        return if (range != null) {
            val (start, end) = range
            if (start != null && end != null) {
                ((end - start) / (1000 * 60 * 60 * 24)).toInt()
            } else {
                0
            }
        } else {
            0
        }
    }



    // ----------------------------------------------------------//
    // Tracking Location Selected
    private val _resortModelResponse = MutableLiveData<ResortModelResponse.Content>()
    val resortModelResponse: MutableLiveData<ResortModelResponse.Content>
        get() = _resortModelResponse
    fun updateResortModel(resortModelResponse: ResortModelResponse.Content){
        _resortModelResponse.value = resortModelResponse
    }


    // ----------------------------------------------------------//
    // Tracking MyTimeshareModel Selected
    private val _myTimeshareResponse = MutableLiveData<MyTimeshareResponse>()
    val myTimeshareModelSelected: MutableLiveData<MyTimeshareResponse>
        get() = _myTimeshareResponse
    // Funtion to update myTimeshareModel
    fun updateMyTimeshareModel(myTimeshareModel: MyTimeshareResponse){
        _myTimeshareResponse.value = myTimeshareModel
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


    // ----------------------------------------------------------//
    // Call Unit Type Detail
    // Init MutableLiveData for Unit Type Detail
    private val _unitTypeDetail = MutableLiveData<Resource<UnitTypeModel>>()
    val unitTypeDetail: MutableLiveData<Resource<UnitTypeModel>> = _unitTypeDetail
    fun getUnitTypeDetail(token: String, unitTypeID: Int) {
        viewModelScope.launch {
            _unitTypeDetail.postValue(Resource.loading(null))
            resortAPIRepository.getUnitTypeDetailById(token, unitTypeID).let {
                _unitTypeDetail.postValue(it)
            }
        }
    }



    // ----------------------------------------------------------//
    // Call List Unit Type of Resort API Selected
    // Init MutableLiveData for List Unit Type
    private val _unitTypeList = MutableLiveData<Resource<List<UnitTypeModel>>>()
    val unitTypeList: MutableLiveData<Resource<List<UnitTypeModel>>> = _unitTypeList
    // Function to get unit type list
    fun getUnitTypeListByResortId(token: String, resortID: Int) {
        viewModelScope.launch {
            _unitTypeList.postValue(Resource.loading(null))
            resortAPIRepository.getUnitTypeListByResortId(token, resortID).let {
                _unitTypeList.postValue(it)
            }
        }
    }


    // ----------------------------------------------------------//
    // Tracking Timeshare DTO
    private val _timeshareDTO = MutableLiveData<Resource<PostingTimeshareResponse>>()
    val timeshareDTO: MutableLiveData<Resource<PostingTimeshareResponse>> = _timeshareDTO

    // Function to post Timeshare DTO
    fun postTimeshareDTO(token: String, timeshareDTO: TimeshareDTO) {
        viewModelScope.launch {
            _timeshareDTO.postValue(Resource.loading(null))
            timeshareRepository.postTimeshare(token, timeshareDTO).let {
                _timeshareDTO.postValue(it)
            }
        }
    }

    // ----------------------------------------------------------//
    // Tracking unit type selection option no
    private val _unitTypeSelectionOptionNo = MutableLiveData<UnitTypeModel>()
    val unitTypeSelectionOptionNo: MutableLiveData<UnitTypeModel>
        get() = _unitTypeSelectionOptionNo
    fun updateUnitTypeSelectionOptionNo(unitTypeModel: UnitTypeModel){
        _unitTypeSelectionOptionNo.value = unitTypeModel
    }

    // ----------------------------------------------------------//
    // Call API create room
    private val _roomModel = MutableLiveData<Resource<PostRoomRespone>>()
    val roomModel: MutableLiveData<Resource<PostRoomRespone>> = _roomModel
    fun postRoom(token: String, roomDTO: RoomDTO) {
        viewModelScope.launch {
            _roomModel.postValue(Resource.loading(null))
            roomAPIRepository.postRoom(token, roomDTO).let {
                _roomModel.postValue(it)
            }
        }
    }


    // ----------------------------------------------------------//
    // Call API get my timeshare list
    private val _myTimeshareList = MutableLiveData<Resource<List<MyTimeshareResponse>>>()
    val myTimeshareList: MutableLiveData<Resource<List<MyTimeshareResponse>>> = _myTimeshareList
    fun getMyTimeshareList(token: String) {
        viewModelScope.launch {
            _myTimeshareList.postValue(Resource.loading(null))
            timeshareRepository.getMyTimeshareList(token).let {
                _myTimeshareList.postValue(it)
            }
        }
    }

    // ----------------------------------------------------------//
    // Call API get my timeshare Detail
    private val _myTimeshareDetail = MutableLiveData<Resource<MyTimeshareDetailResponse>>()
    val myTimeshareDetail: MutableLiveData<Resource<MyTimeshareDetailResponse>> = _myTimeshareDetail
    fun getMyTimeshareDetail(token: String, timeShareId: Int) {
        viewModelScope.launch {
            _myTimeshareDetail.postValue(Resource.loading(null))
            timeshareRepository.getMyTimeshareDetail(token, timeShareId).let {
                _myTimeshareDetail.postValue(it)
            }
        }
    }





    // Tracking Yes or No for Step 2
    private val isYesOrNo = MutableLiveData<Boolean>()
    val isYesOrNoSelected: MutableLiveData<Boolean>
        get() = isYesOrNo
    fun updateIsYesOrNo(isYesOrNo: Boolean){
        this.isYesOrNo.value = isYesOrNo
    }




    // Init
    init {
        _step.value = initStep
        _currentStepInProgress.value = initStep
        _stepCreateTimeshare.value = 0
        isYesOrNo.value = false
    }





}