package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BookingDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingDetailResponse
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
}