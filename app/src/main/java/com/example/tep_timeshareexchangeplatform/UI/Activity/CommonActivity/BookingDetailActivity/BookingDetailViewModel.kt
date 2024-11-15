package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BookingDetailActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Feedback.FeedbackResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingDetailViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call Get My Booking Response
    private val _getMyBookingDetailResponse = MutableLiveData<Resource<MyBookingDetailResponse>>()
    val getMyBookingDetailResponse: MutableLiveData<Resource<MyBookingDetailResponse>>
        get() = _getMyBookingDetailResponse

    fun getMyBookingDetail(token: String, bookingId: Int) {
        viewModelScope.launch {
            _getMyBookingDetailResponse.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerBookingDetail(token, bookingId).let {
                _getMyBookingDetailResponse.postValue(it)
            }
        }
    }

    /**
     * Call API To POST FeedBack
     *
     */
    private var _feedbackResponse = MutableLiveData<Resource<FeedbackResponse>>()
    val feedbackResponse: LiveData<Resource<FeedbackResponse>> = _feedbackResponse
    fun postFeedback(token: String, feedbackDTO: FeedbackDTO) {
        viewModelScope.launch {
            _feedbackResponse.postValue(Resource.loading(null))
            customerAPIRepository.postFeedbackForCustomerRental(token, feedbackDTO).let {
                _feedbackResponse.postValue(it)
            }
        }
    }
}