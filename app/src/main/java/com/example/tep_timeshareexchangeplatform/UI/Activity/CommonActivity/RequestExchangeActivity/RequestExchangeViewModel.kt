package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.RequestExchangeActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestExchangeViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Get Exchange Posting Detail By ID
    private val _exchangePostingDetail = MutableLiveData<Resource<ExchangeDetailResponse>>()
    val exchangePostingDetail: MutableLiveData<Resource<ExchangeDetailResponse>> =
        _exchangePostingDetail

    fun callGetExchangePostingDetail(postingId: Int) {
        viewModelScope.launch {
            _exchangePostingDetail.postValue(Resource.loading(null))
            publicPostingAPIRepository.getExchangePostingDetail(postingId).let {
                _exchangePostingDetail.postValue(it)
            }
        }
    }

    // Get My Timeshare Detail
    private val _myTimeshareDetail = MutableLiveData<Resource<MyTimeshareDetailResponse>>()
    val myTimeshareDetail: MutableLiveData<Resource<MyTimeshareDetailResponse>> =
        _myTimeshareDetail

    fun getMyTimeshareDetail(token: String, timeShareId: Int) {
        viewModelScope.launch {
            _myTimeshareDetail.postValue(Resource.loading(null))
            customerAPIRepository.getTimeShareDetail(token, timeShareId).let {
                _myTimeshareDetail.postValue(it)
            }
        }
    }

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

    // Tracking Number Of Nights
    private val _numberOfNights = MutableLiveData<Int>()
    val numberOfNights: MutableLiveData<Int>
        get() = _numberOfNights

    fun updateNumberOfNights(numberOfNights: Int) {
        _numberOfNights.value = numberOfNights
    }

    private val _currentTimeshareSelected = MutableLiveData<Int>()
    val currentTimeshareSelected: MutableLiveData<Int> get() = _currentTimeshareSelected
    fun setCurrentTimeshareSelected(timeshareId: Int) {
        _currentTimeshareSelected.value = timeshareId
    }

    fun getCurrentTimeshareIdSelected(): Int? {
        return _currentTimeshareSelected.value
    }

    // Send Exchange Request
    private val _exchangeRequestResponse = MutableLiveData<Resource<ExchangeRequestResponse>>()
    val exchangeRequestResponse: MutableLiveData<Resource<ExchangeRequestResponse>> =
        _exchangeRequestResponse

    fun callCreateExchangeRequest(token: String, postingId: Int, exchangeRequestDTO: ExchangeRequestDTO) {
        viewModelScope.launch {
            _exchangeRequestResponse.postValue(Resource.loading(null))
            customerAPIRepository.sendExchangeRequest(token, postingId, exchangeRequestDTO).let {
                _exchangeRequestResponse.postValue(it)
            }
        }
    }


    private val _price = MutableLiveData<Long>()
    val price: MutableLiveData<Long>
        get() = _price

    fun updatePrice(price: Long) {
        _price.value = price
    }

    private val _priceForRequest = MutableLiveData<Long>()
    val priceForRequest: MutableLiveData<Long>
        get() = _priceForRequest
    fun updatePriceForRequest(price: Long) {
        _priceForRequest.value = price
    }



    init {
        _price.value = 0
    }


}