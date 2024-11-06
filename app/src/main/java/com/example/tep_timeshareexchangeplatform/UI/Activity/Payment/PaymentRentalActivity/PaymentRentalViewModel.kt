package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PaymentAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.GuestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentRentalViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val paymentAPIRepository: PaymentAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    // Call get Posting Detail API
    private val _postingDetail = MutableLiveData<Resource<PublicPostingDetailResponse>>()
    val postingDetail: MutableLiveData<Resource<PublicPostingDetailResponse>> = _postingDetail
    fun getPostingDetail(postingId: Int) {
        viewModelScope.launch {
            _postingDetail.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostingDetail(postingId).let {
                _postingDetail.postValue(it)
            }
        }
    }

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

    // Call API Create Booking
    private val _myBookingResponse = MutableLiveData<Resource<MyBookingDetailResponse>>()
    val myBookingResponse: MutableLiveData<Resource<MyBookingDetailResponse>> = _myBookingResponse
    fun createBooking(token: String, postingId: Int, guestDTO: GuestDTO) {
        viewModelScope.launch {
            _myBookingResponse.postValue(Resource.loading(null))
            customerAPIRepository.createBookingRequest(token, postingId, guestDTO).let {
                _myBookingResponse.postValue(it)
            }
        }
    }



    // LiveData to hold the current page index
    private val _currentViewPager = MutableLiveData<Int>()
    val currentViewPager: MutableLiveData<Int>
        get() = _currentViewPager

    // Function to set the current page
    fun setCurrentViewPager(page: Int) {
        _currentViewPager.value = page
    }

    // Function to get the current page as LiveData
    fun getCurrentViewPager(): LiveData<Int> {
        return _currentViewPager
    }


    // Tracking Guest DTO
    private val _guestDTO = MutableLiveData<GuestDTO>()
    val guestDTO: MutableLiveData<GuestDTO> = _guestDTO
    fun setGuestDTO(guestDTO: GuestDTO) {
        _guestDTO.value = guestDTO
    }


    // Biến LiveData để theo dõi phương thức thanh toán
    private val _selectedPaymentMethod = MutableLiveData<PaymentMethod>()
    val selectedPaymentMethod: LiveData<PaymentMethod> get() = _selectedPaymentMethod

    init {
        _selectedPaymentMethod.value = PaymentMethod.VNPAY
    }

    // Hàm để chọn phương thức thanh toán
    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

}