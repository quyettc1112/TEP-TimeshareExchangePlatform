package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PaymentAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.RoomAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare.PostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.PostRoomRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyPostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletPurchaseResponse
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentalPostingViewModel @Inject constructor(
    private val roomAPIRepository: RoomAPIRepository,
    private val publicResortAPIRepository: PublicResortAPIRepository,
    private val timeshareRepository: TimeshareRepository,
    private val customerAPIRepository: CustomerAPIRepository,
    private val paymentAPIRepository: PaymentAPIRepository,
    private val walletAPIRepository: WalletAPIRepository
) : ViewModel() {

    private val initStep: Int = 1


    // ----------------------------------------------------------//
    // Tracking Progress Step
    private val _step = MutableLiveData<Int>()
    val step: MutableLiveData<Int>
        get() = _step

    fun updateStep(step: Int) {
        if (step >= _currentStepInProgress.value!!) {
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
    fun updateCurrentStepInProgress(step: Int) {
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

    fun updateResortModel(resortModelResponse: ResortModelResponse.Content) {
        _resortModelResponse.value = resortModelResponse
    }


    // ----------------------------------------------------------//
    // Tracking MyTimeshareModel Selected
    private val _myTimeshareResponse = MutableLiveData<MyTimeshareResponse.Content>()
    val myTimeshareModelSelected: MutableLiveData<MyTimeshareResponse.Content>
        get() = _myTimeshareResponse

    // Funtion to update myTimeshareModel
    fun updateMyTimeshareModel(myTimeshareModel: MyTimeshareResponse.Content) {
        _myTimeshareResponse.value = myTimeshareModel
    }


    // ----------------------------------------------------------//
    // Tracking Package Step 4 Selected
    private val _packageStep4 = MutableLiveData<PackageModel>()
    val packageStep4: MutableLiveData<PackageModel>
        get() = _packageStep4

    // Funtion to update packageStep4
    fun updatePackageStep4(packageModel: PackageModel) {
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
            publicResortAPIRepository.getUnitTypeDetailById(token, unitTypeID).let {
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
            publicResortAPIRepository.getUnitTypeListByResortId(token, resortID).let {
                _unitTypeList.postValue(it)
            }
        }
    }


    // ----------------------------------------------------------//
    // Tracking Timeshare DTO
    private val _timeshareDTO = MutableLiveData<Resource<MyPostingTimeshareResponse>>()
    val timeshareDTO: MutableLiveData<Resource<MyPostingTimeshareResponse>> = _timeshareDTO

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

    fun updateUnitTypeSelectionOptionNo(unitTypeModel: UnitTypeModel) {
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
    private val _myTimeshareList = MutableLiveData<Resource<MyTimeshareResponse>>()
    val myTimeshareList: MutableLiveData<Resource<MyTimeshareResponse>> = _myTimeshareList
    fun getMyTimeshareList(token: String, page: Int, size: Int) {
        viewModelScope.launch {
            _myTimeshareList.postValue(Resource.loading(null))
            timeshareRepository.getMyTimeshareList(token, page, size).let {
                _myTimeshareList.postValue(it)
            }
        }
    }

    // Check Current Posting Page
    private var _currentMyTimesharePage = MutableLiveData<Int>()
    val currentMyTimesharePage: MutableLiveData<Int>
        get() = _currentMyTimesharePage

    fun incrementCurrentMyTimesharePage() {
        val currentValue = _currentMyTimesharePage.value ?: 0
        _currentMyTimesharePage.value = currentValue + 1
    }


    private val _currentMyTimeshareList = mutableListOf<MyTimeshareResponse.Content>()
    fun loadMoreTimeshareList(list: List<MyTimeshareResponse.Content>) {
        _currentMyTimeshareList.addAll(list)
    }

    fun getCurrentMyTimeshareList(): List<MyTimeshareResponse.Content> {
        return _currentMyTimeshareList
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

    // ----------------------------------------------------------//
    // Call API get valid year timeshare of Customer
    private val _validYearTimeshare = MutableLiveData<Resource<ValidYearResponse>>()
    val validYearTimeshare: MutableLiveData<Resource<ValidYearResponse>> = _validYearTimeshare
    fun getValidYearTimeshare(token: String, timeShareId: Int) {
        viewModelScope.launch {
            _validYearTimeshare.postValue(Resource.loading(null))
            customerAPIRepository.getValidYearTimeshare(token, timeShareId).let {
                _validYearTimeshare.postValue(it)
            }
        }
    }

    // ----------------------------------------------------------//
    // CREATE POSTING
    // Tracking PricePerNight
    private val _pricePerNight = MutableLiveData<Long>()
    val pricePerNight: MutableLiveData<Long>
        get() = _pricePerNight

    fun updatePricePerNight(pricePerNight: Long) {
        _pricePerNight.value = pricePerNight
    }

    // ----------------------------------------------------------//
    // Tracking Number Of Nights
    private val _numberOfNights = MutableLiveData<Int>()
    val numberOfNights: MutableLiveData<Int>
        get() = _numberOfNights

    fun updateNumberOfNights(numberOfNights: Int) {
        _numberOfNights.value = numberOfNights
    }

    // ----------------------------------------------------------//
    // Tracking Start Date, End Date
    // LiveData để lưu giá trị ngày check-in và check-out
    private val _checkinDate = MutableLiveData<String>()
    val checkinDate: LiveData<String> get() = _checkinDate

    private val _checkoutDate = MutableLiveData<String>()
    val checkoutDate: LiveData<String> get() = _checkoutDate

    // Phương thức để cập nhật giá trị ngày check-in
    fun setCheckinDate(date: String) {
        _checkinDate.value = date
    }

    // Phương thức để cập nhật giá trị ngày check-out
    fun setCheckoutDate(date: String) {
        _checkoutDate.value = date
    }

    // ----------------------------------------------------------//
    // Cancel Policy
    private val _cancelPolicy = MutableLiveData<Int>()
    val cancelPolicy: MutableLiveData<Int>
        get() = _cancelPolicy

    fun updateCancelPolicy(cancelPolicy: Int) {
        _cancelPolicy.value = cancelPolicy
    }


    // Payment Method
    // Biến LiveData để theo dõi phương thức thanh toán
    private val _selectedPaymentMethod = MutableLiveData<PaymentMethod>()
    val selectedPaymentMethod: LiveData<PaymentMethod> get() = _selectedPaymentMethod

    // Hàm để chọn phương thức thanh toán
    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

    // ----------------------------------------------------------//
    // Call VNPAY API
    private val _responseVNPAYUrl = MutableLiveData<Resource<PaymentResponse>>()
    val responseVNPAYUrl: MutableLiveData<Resource<PaymentResponse>> = _responseVNPAYUrl

    // call API to get response URL
    fun getResponsePaymentUrl(amount: Int, orderType: String) {
        viewModelScope.launch {
            _responseVNPAYUrl.postValue(Resource.loading(null))
            paymentAPIRepository.getPaymentUrl(amount, orderType).let {
                _responseVNPAYUrl.postValue(it)
            }
        }
    }

    // ----------------------------------------------------------//
    // Call API Purchase Package by Wallet
    private val _walletPurchaseResponse = MutableLiveData<Resource<WalletPurchaseResponse>>()
    val walletPurchaseResponse: MutableLiveData<Resource<WalletPurchaseResponse>> =
        _walletPurchaseResponse

    fun purchasePackagePostingWallet(token: String, rentalPackageId: Int) {
        viewModelScope.launch {
            _walletPurchaseResponse.postValue(Resource.loading(null))
            walletAPIRepository.purchasePackagePostingWallet(token, rentalPackageId).let {
                _walletPurchaseResponse.postValue(it)
            }
        }
    }

    // ----------------------------------------------------------//
    // Call API Get New Balance
    private val _customerInfoResponse = MutableLiveData<Resource<CustomerInfoResponse>>()
    val newBalanceInfoResponse: MutableLiveData<Resource<CustomerInfoResponse>> =
        _customerInfoResponse

    fun getCustomerInfo(token: String) {
        viewModelScope.launch {
            _customerInfoResponse.postValue(Resource.loading(null))
            customerAPIRepository.getIsCustomerExist(token).let {
                _customerInfoResponse.postValue(it)
            }
        }
    }

    // ----------------------------------------------------------//
    // Call API Create Posting
    private val _postingTimeshareResponse = MutableLiveData<Resource<PostingTimeshareResponse>>()
    val postingTimeshareResponse: MutableLiveData<Resource<PostingTimeshareResponse>> =
        _postingTimeshareResponse

    fun createPosting(token: String, postingTimeshareResponse: PostingTimeshareDTO) {
        viewModelScope.launch {
            _postingTimeshareResponse.postValue(Resource.loading(null))
            customerAPIRepository.createPosting(token, postingTimeshareResponse).let {
                _postingTimeshareResponse.postValue(it)
            }
        }
    }


    // Tracking Yes or No for Step 2
    private val isYesOrNo = MutableLiveData<Boolean>()
    val isYesOrNoSelected: MutableLiveData<Boolean>
        get() = isYesOrNo

    fun updateIsYesOrNo(isYesOrNo: Boolean) {
        this.isYesOrNo.value = isYesOrNo
    }


    // Init
    init {
        _step.value = initStep
        _currentStepInProgress.value = initStep
        _stepCreateTimeshare.value = 0
        isYesOrNo.value = false
        _selectedPaymentMethod.value = PaymentMethod.VNPAY

        _currentMyTimesharePage.value = 0
    }


}