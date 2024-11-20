package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PaymentAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.RoomAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.StorageAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.TimeshareRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare.PostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.PostRoomRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyPostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletPurchaseResponse
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PostingFlowViewModel @Inject constructor(
    private val roomAPIRepository: RoomAPIRepository,
    private val publicResortAPIRepository: PublicResortAPIRepository,
    private val timeshareRepository: TimeshareRepository,
    private val customerAPIRepository: CustomerAPIRepository,
    private val paymentAPIRepository: PaymentAPIRepository,
    private val walletAPIRepository: WalletAPIRepository,
    private val storageAPIRepository: StorageAPIRepository
) : ViewModel() {

    private val initStep: Int = 1

    // Tracking Type of Posting Flow
    private val _typeOfPostingFlow = MutableLiveData<String>()
    val typeOfPostingFlow: MutableLiveData<String>
        get() = _typeOfPostingFlow

    fun updateTypeOfPostingFlow(type: String) {
        _typeOfPostingFlow.value = type
    }

    fun getTypeOfPostingFlow(): String {
        return _typeOfPostingFlow.value ?: ""
    }


    // ----------------------------------------------------------//
    // Tracking Progress Step
    private val _step = MutableLiveData<Int>()
    val step: MutableLiveData<Int>
        get() = _step

    fun updateStep(step: Int) {
        // Kiểm tra xem giá trị mới có khác giá trị hiện tại không để tránh cập nhật không cần thiết
        if (_step.value == step) {
            return
        }

        if (step >= _currentStepInProgress.value ?: 0) {
            updateCurrentStepInProgress(step)
        }

        _step.value = step

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
    private val _startDateTimeshare = MutableLiveData<String>()
    val startDateTimeshare: LiveData<String> get() = _startDateTimeshare

    private val _endDateTimeshare = MutableLiveData<String>()
    val endDateTimeshare: LiveData<String> get() = _endDateTimeshare

    fun setTimeshareDateRange(start: Long?, end: Long?) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        _startDateTimeshare.value = start?.let { dateFormat.format(Date(it)) }
        _endDateTimeshare.value = end?.let { dateFormat.format(Date(it)) }

        if (start != null && end != null) {
            val numberOfDays = ((end - start) / (1000 * 60 * 60 * 24)).toInt()
            _numberOfNightsTimeShare.value = numberOfDays
        } else {
            _numberOfNightsTimeShare.value = 0
        }
    }

    fun getTimeshareDateRange(): Pair<String, String> {
        return Pair(_startDateTimeshare.value ?: "", _endDateTimeshare.value ?: "")
    }


    private val _numberOfNightsTimeShare = MutableLiveData<Int>()
    val numberOfNightsTimeshare: LiveData<Int> get() = _numberOfNightsTimeShare
    fun getNumberOfNightsTimeshare(): Int {
        return _numberOfNightsTimeShare.value ?: 0
    }

    fun resetTimeshareDateRange() {
        _startDateTimeshare.value = ""
        _endDateTimeshare.value = ""
        _numberOfNightsTimeShare.value = 0
    }

    // ----------------------------------------------------------//

    private val _yearRange = MutableLiveData<Pair<Int, Int>>()
    val yearRange: LiveData<Pair<Int, Int>> get() = _yearRange

    fun setYearRange(startYear: Int, endYear: Int) {
        _yearRange.value = Pair(startYear, endYear)
    }

    fun getYearRange(): Pair<Int, Int> {
        return (_yearRange.value ?: Pair(0, 0)) as Pair<Int, Int>
    }

    fun resetTimeshareYearRange() {
        _yearRange.value = Pair(0, 0)
    }


    // ----------------------------------------------------------//
    // Exchange Date Selected
    // LiveData to hold the pair of start and end dates
    private val _exchangeDateRange = MutableLiveData<Pair<Long, Long>>()
    val exchangeDateRange: LiveData<Pair<Long, Long>> get() = _exchangeDateRange
    fun setExchangeDateRange(startDate: Long, endDate: Long) {
        _exchangeDateRange.value = Pair(startDate, endDate)
    }

    fun getExchangeDateRange(): Pair<Long, Long> {
        return (_exchangeDateRange.value ?: Pair(null, null)) as Pair<Long, Long>
    }

    fun getNumberOfExchangeNights(): Int {
        val range = _exchangeDateRange.value
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

    // Check Current Province Selected
    private var _currentProvinceSelected = MutableLiveData<Int>()
    val currentProvinceSelected: MutableLiveData<Int>
        get() = _currentProvinceSelected

    fun updateCurrentProvinceSelected(currentProvince: Int) {
        _currentProvinceSelected.value = currentProvince
    }

    fun getCurrentProvinceSelected(): Int {
        return _currentProvinceSelected.value ?: 0
    }


    // Ghi Chu
    private val _note = MutableLiveData<String?>()
    val note: MutableLiveData<String?>
        get() = _note

    fun updateNoteContent(note: String?) {
        _note.value = note
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

    fun getPackageStep4(): PackageModel? {
        return _packageStep4.value
    }


    // ----------------------------------------------------------//
    // Call List Room of Resort API Selected
    // Init MutableLiveData for resort list
    private val _roomList = MutableLiveData<Resource<List<RoomModel>>>()
    val roomList: MutableLiveData<Resource<List<RoomModel>>> = _roomList

    // Function to get Room List By Resort ID
    fun getRoomListByResortId(token: String, resortID: Int) {
        viewModelScope.launch {
            _roomList.postValue(Resource.loading(null))
            roomAPIRepository.getRoomListByResortId(token, resortID).let {
                _roomList.postValue(it)
            }
        }
    }

    private val _roomDetailResponse = MutableLiveData<Resource<RoomDetailResponse>?>()
    val roomDetailResponse: MutableLiveData<Resource<RoomDetailResponse>?> = _roomDetailResponse
    fun getRoomDetailById(token: String, roomId: Int) {
        viewModelScope.launch {
            _roomDetailResponse.postValue(Resource.loading(null))
            customerAPIRepository.getRoomDetailById(token, roomId).let {
                _roomDetailResponse.postValue(it)
            }
        }
    }

    fun clearRoomDetailResponse() {
        _roomDetailResponse.value = null
    }


    // ----------------------------------------------------------//
    // Call Unit Type Detail
    // Init MutableLiveData for Unit Type Detail
    private val _unitTypeDetail = MutableLiveData<Resource<UnitTypeModel>>()
    val unitTypeDetail: MutableLiveData<Resource<UnitTypeModel>> = _unitTypeDetail
    fun getUnitTypeDetailByID(token: String, unitTypeID: Int) {
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
    private val _unitTypeList = MutableLiveData<Resource<List<UnitTypeModel>>?>()
    val unitTypeList: MutableLiveData<Resource<List<UnitTypeModel>>?> = _unitTypeList

    fun cleanUnitTypeList() {
        _unitTypeList.value = null
    }

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
    val createTimeshareResponse: MutableLiveData<Resource<MyPostingTimeshareResponse>> =
        _timeshareDTO

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
    private val _unitTypeSelectionOptionNo = MutableLiveData<UnitTypeModel?>()
    val unitTypeSelectionOptionNo: MutableLiveData<UnitTypeModel?>
        get() = _unitTypeSelectionOptionNo

    fun updateUnitTypeSelectionOptionNo(unitTypeModel: UnitTypeModel) {
        _unitTypeSelectionOptionNo.value = unitTypeModel
    }
    fun getUnitTypeSelectionOptionNo(): UnitTypeModel? {
        return _unitTypeSelectionOptionNo.value ?: null
    }
    fun resetUnitTypeSelectionOptionNo() {
        _unitTypeSelectionOptionNo.value  = null
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

    // Lưu trạng thái tiện ích theo từng loại (type)
    private val _selectedAmenities = MutableLiveData<Map<AmenityType, List<AmenitiesModel>>>()
    val selectedAmenities: LiveData<Map<AmenityType, List<AmenitiesModel>>> get() = _selectedAmenities


    fun updateAmenitiesForType(type: AmenityType, selectedAmenities: List<AmenitiesModel>) {
        val currentMap = _selectedAmenities.value?.toMutableMap() ?: mutableMapOf()
        currentMap[type] = selectedAmenities // Lưu toàn bộ danh sách, không chỉ các mục được chọn
        _selectedAmenities.value = currentMap
        Log.d("ViewModelUpdate", "Updated Type: $type, Data: ${currentMap[type]}")
    }

    fun getSelectedAmenitiesALl(): List<AmenitiesModel> {
        return _selectedAmenities.value?.flatMap { it.value } ?: emptyList()
    }


    fun getSelectedAmenitiesForPost(): List<RoomDTO.RoomAmenity> {
        return _selectedAmenities.value
            ?.flatMap { (type, amenities) ->
                amenities.filter { it.isChecked }.map { RoomDTO.RoomAmenity(it.name, type.name) }
            }
            ?: emptyList()
    }
    fun isValidSelection(): Boolean {
        // Nhóm danh sách các mục đã chọn theo `type`
        val selectedAmenities: List<RoomDTO.RoomAmenity> = getSelectedAmenitiesForPost()
        val groupedAmenities = selectedAmenities.groupBy { it.type }

        // Kiểm tra từng loại (FEATURES, ENTERTAINMENT, KITCHEN)
        val isFeaturesValid = groupedAmenities["FEATURES"]?.size ?: 0 >= 2
        val isEntertainmentValid = groupedAmenities["ENTERTAINMENT"]?.size ?: 0 >= 2
        val isKitchenValid = groupedAmenities["KITCHEN"]?.size ?: 0 >= 2

        // Không cần kiểm tra POLICY
        return isFeaturesValid && isEntertainmentValid && isKitchenValid
    }

    fun clearAllAmenities() {
        _selectedAmenities.value = AmenityType.values().associateWith { emptyList() }
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

    fun clearCurrentMyTimeshareList() {
        _currentMyTimeshareList.clear()
    }

    private val _isStep3Visible = MutableLiveData<Boolean>()
    val isStep3Visible: LiveData<Boolean> get() = _isStep3Visible.distinctUntilChanged()

    fun updateStep3Visibility(isVisible: Boolean) {
        _isStep3Visible.value = isVisible
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

    fun getSelectedPaymentMethod(): PaymentMethod {
        return _selectedPaymentMethod.value ?: PaymentMethod.VNPAY
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

    fun getVNPAYUrl(): String {
        return _responseVNPAYUrl.value?.data?.url ?: ""
    }

    // ----------------------------------------------------------//
    // Call API Purchase Package by Wallet
    private val _walletPurchaseResponse = MutableLiveData<Resource<WalletPurchaseResponse>>()
    val createRentalPostingTransactionByWallet: MutableLiveData<Resource<WalletPurchaseResponse>> =
        _walletPurchaseResponse

    fun createRentalPostingTransactionByWallet(token: String, rentalPackageId: Int) {
        viewModelScope.launch {
            _walletPurchaseResponse.postValue(Resource.loading(null))
            walletAPIRepository.purchasePackagePostingWallet(token, rentalPackageId).let {
                _walletPurchaseResponse.postValue(it)
            }
        }
    }

    private val _createExchangePostingByWallet = MutableLiveData<Resource<WalletPurchaseResponse>>()
    val createExchangePostingTransactionByWallet: MutableLiveData<Resource<WalletPurchaseResponse>> =
        _createExchangePostingByWallet

    fun createExchangePostingTransactionByWallet(token: String, exchangePackageId: Int) {
        viewModelScope.launch {
            _createExchangePostingByWallet.postValue(Resource.loading(null))
            walletAPIRepository.purchasePackagePostingWallet(token, exchangePackageId).let {
                _createExchangePostingByWallet.postValue(it)
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
    val createRentalPosting: MutableLiveData<Resource<PostingTimeshareResponse>> =
        _postingTimeshareResponse

    fun createRentalPosting(token: String, postingTimeshareResponse: PostingTimeshareDTO) {
        viewModelScope.launch {
            _postingTimeshareResponse.postValue(Resource.loading(null))
            customerAPIRepository.createPosting(token, postingTimeshareResponse).let {
                _postingTimeshareResponse.postValue(it)
            }
        }
    }


    private val _createExchangePosting = MutableLiveData<Resource<PostingTimeshareResponse>>()
    val createExchangePosting: MutableLiveData<Resource<PostingTimeshareResponse>> =
        _createExchangePosting

    fun createExchangePosting(token: String, exchangeTimeshareDTO: ExchangeTimeshareDTO) {
        viewModelScope.launch {
            _createExchangePosting.postValue(Resource.loading(null))
            customerAPIRepository.createExchangePosting(token, exchangeTimeshareDTO).let {
                _createExchangePosting.postValue(it)
            }
        }
    }


    // Tracking Yes or No for Step 2
    private val isYesOrNo = MutableLiveData<Boolean>()
    val isYesOrNoSelected: MutableLiveData<Boolean>
        get() = isYesOrNo

    fun getIsYesOrNoSelected(): Boolean {
        return isYesOrNo.value ?: false
    }
    fun updateIsYesOrNo(isYesOrNo: Boolean) {
        this.isYesOrNo.value = isYesOrNo
    }

    // ----------------------------------------------------------//
    private val _currentRoomInfo = MutableLiveData<Int>()
    val currentRoomInfo: MutableLiveData<Int>
        get() = _currentRoomInfo

    fun updateCurrentRoomInfo(currentRoomInfo: Int) {
        _currentRoomInfo.value = currentRoomInfo
    }

    fun resetData() {
        // Reset tất cả các LiveData hoặc MutableLiveData trong ExchangeOfResortViewModel
        _currentMyTimesharePage.value = 0
        _currentMyTimeshareList.clear()
        step.value = 1 // hoặc giá trị mặc định ban đầu
        // Reset các giá trị khác nếu cần
    }

    // ----------------------------------------------------------//
    // Image URI, Bind To MutiplePart
    private val _imageList = MutableLiveData<List<ImageUploadModel>>(emptyList())
    val imageList: LiveData<List<ImageUploadModel>> get() = _imageList

    // Đặt ảnh chính
    fun setMainImage(mainImage: ImageUploadModel) {
        _imageList.value = _imageList.value?.toMutableList()?.apply {
            // Kiểm tra nếu ảnh chính đã tồn tại, xóa nó
            remove(mainImage)
            // Thêm ảnh chính vào đầu danh sách
            add(0, mainImage)
        } ?: listOf(mainImage) // Nếu danh sách rỗng, khởi tạo với ảnh chính
    }

    fun addImages(newImages: List<ImageUploadModel>) {
        _imageList.value = _imageList.value?.toMutableList()?.apply {
            addAll(newImages)
        }
    }

    fun deleteImage(image: ImageUploadModel) {
        _imageList.value = _imageList.value?.toMutableList()?.apply {
            remove(image)
        }
    }

    fun getMultipartBodies(): List<MultipartBody.Part> {
        return _imageList.value?.map { it.part } ?: emptyList()
    }

    private val _listImageResponse = MutableLiveData<Resource<List<String>>>()
    val uploadImageResponse: LiveData<Resource<List<String>>> get() = _listImageResponse
    fun callUploadImages(token: String) {
        viewModelScope.launch {
            _listImageResponse.postValue(Resource.loading(null))
            val images = getMultipartBodies()
            val response = storageAPIRepository.uploadFiles(token, images)
            _listImageResponse.postValue(response)
        }
    }

    fun getUploadedImageUrls(): List<String> {
        return _listImageResponse.value?.data ?: emptyList()
    }


    // Init
    init {
        _step.value = initStep
        _currentStepInProgress.value = initStep
        _stepCreateTimeshare.value = 0
        isYesOrNo.value = false
        _selectedPaymentMethod.value = PaymentMethod.VNPAY
        _currentRoomInfo.value = 0

        // Khởi tạo map rỗng cho từng loại
        _selectedAmenities.value = AmenityType.values().associateWith { emptyList() }
    }


}