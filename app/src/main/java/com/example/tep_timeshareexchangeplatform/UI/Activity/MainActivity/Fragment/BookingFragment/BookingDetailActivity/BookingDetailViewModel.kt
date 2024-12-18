package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment.BookingDetailActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.UpdateExchangeBookingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.CancelBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingRentalDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Feedback.FeedbackResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingDetailViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call Get My Booking Response
    private val _getMyBookingRentalDetailResponse = MutableLiveData<Resource<MyBookingRentalDetailResponse>>()
    val getMyBookingRentalDetailResponse: MutableLiveData<Resource<MyBookingRentalDetailResponse>>
        get() = _getMyBookingRentalDetailResponse

    fun getMyBookingRentalDetail(token: String, bookingId: Int) {
        viewModelScope.launch {
            _getMyBookingRentalDetailResponse.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerBookingDetail(token, bookingId).let {
                _getMyBookingRentalDetailResponse.postValue(it)
            }
        }
    }


    // Call get My Booking Exchange Detail Response
    private val _getMyBookingExchangeDetailResponse = MutableLiveData<Resource<MyBookingExchangeDetailResponse>>()
    val getMyBookingExchangeDetailResponse: MutableLiveData<Resource<MyBookingExchangeDetailResponse>>
        get() = _getMyBookingExchangeDetailResponse

    fun getMyBookingExchangeDetail(token: String, bookingId: Int) {
        viewModelScope.launch {
            _getMyBookingExchangeDetailResponse.postValue(Resource.loading(null))
            customerAPIRepository.getMyBookingExchange(token, bookingId).let {
                _getMyBookingExchangeDetailResponse.postValue(it)
            }
        }
    }

    /**
     * Call API To POST FeedBack
     *
     */
    private var _feedbackRentalResponse = MutableLiveData<Resource<FeedbackResponse>>()
    val feedbackRentalResponse: LiveData<Resource<FeedbackResponse>> = _feedbackRentalResponse
    fun postFeedbackRental(token: String, feedbackDTO: FeedbackDTO) {
        viewModelScope.launch {
            _feedbackRentalResponse.postValue(Resource.loading(null))
            customerAPIRepository.postFeedbackForCustomerRental(token, feedbackDTO).let {
                _feedbackRentalResponse.postValue(it)
            }
        }
    }

    private var _feedbackExchangeResponse = MutableLiveData<Resource<FeedbackResponse>>()
    val feedbackExchangeResponse: LiveData<Resource<FeedbackResponse>> = _feedbackExchangeResponse
    fun postFeedbackExchange(token: String, feedbackDTO: FeedbackDTO) {
        viewModelScope.launch {
            _feedbackExchangeResponse.postValue(Resource.loading(null))
            customerAPIRepository.postFeedbackForCustomerExchange(token, feedbackDTO).let {
                _feedbackExchangeResponse.postValue(it)
            }
        }
    }

    // Call API To Cancel Booking
    private var _cancelBookingResponse = MutableLiveData<Resource<CancelBookingResponse>>()
    val cancelBookingResponse: LiveData<Resource<CancelBookingResponse>> = _cancelBookingResponse
    fun cancelBooking(token: String, bookingId: Int) {
        viewModelScope.launch {
            _cancelBookingResponse.postValue(Resource.loading(null))
            customerAPIRepository.cancelBookingRequest(token, bookingId).let {
                _cancelBookingResponse.postValue(it)
            }
        }
    }
    
    // Call API Update Customer Info
    private var _updateExchangeBookingInfoResponse = MutableLiveData<Resource<Void>>()
    val updateExchangeBookingInfoResponse: LiveData<Resource<Void>> = _updateExchangeBookingInfoResponse
    fun updateExchangeBookingInfo(token: String, bookingId: Int, updateExchangeBookingDTO: UpdateExchangeBookingDTO) {
        viewModelScope.launch {
            _updateExchangeBookingInfoResponse.postValue(Resource.loading(null))
            customerAPIRepository.updateExchangeBookingCustomerInfo(token, bookingId, updateExchangeBookingDTO).let {
                _updateExchangeBookingInfoResponse.postValue(it)
            }
        }
    }

    // Call Get Customer Profile Response
    private val _getCustomerProfileResponse = MutableLiveData<Resource<CustomerProfileResponse>>()
    val getCustomerProfileResponse: MutableLiveData<Resource<CustomerProfileResponse>>
        get() = _getCustomerProfileResponse
    fun callGetCustomerProfile(token: String) {
        viewModelScope.launch {
            _getCustomerProfileResponse.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerProfile(token).let {
                _getCustomerProfileResponse.postValue(it)
            }
        }
    }




}