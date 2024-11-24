package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PaymentAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.GuestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingRentalDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletPurchaseResponse
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentRentalViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val paymentAPIRepository: PaymentAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository,
    private val walletAPIRepository: WalletAPIRepository
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
    fun getResponsePaymentUrl(amount: Long, orderType: String) {
        viewModelScope.launch {
            _responseVNPAYUrl.postValue(Resource.loading(null))
            paymentAPIRepository.getPaymentUrl(amount, orderType).let {
                _responseVNPAYUrl.postValue(it)
            }
        }
    }

    // Call API Create Booking
    private val _myBookingResponse = MutableLiveData<Resource<MyBookingRentalDetailResponse>>()
    val myBookingResponse: MutableLiveData<Resource<MyBookingRentalDetailResponse>> = _myBookingResponse
    fun createBooking(token: String, postingId: Int, guestDTO: GuestDTO) {
        viewModelScope.launch {
            _myBookingResponse.postValue(Resource.loading(null))
            customerAPIRepository.createBookingRequest(token, postingId, guestDTO).let {
                _myBookingResponse.postValue(it)
            }
        }
    }

    // Call API Booking By Wallet
    private val _walletPurchaseResponse = MutableLiveData<Resource<WalletPurchaseResponse>>()
    val walletPurchaseResponse: MutableLiveData<Resource<WalletPurchaseResponse>> = _walletPurchaseResponse
    fun bookingByWallet(token: String, postingId: Int) {
        viewModelScope.launch {
            _walletPurchaseResponse.postValue(Resource.loading(null))
            walletAPIRepository.bookingRentalWallet(token, postingId).let {
                _walletPurchaseResponse.postValue(it)
            }
        }
    }

    // Call Get New Available Balance
    private val _customerInfoResponse = MutableLiveData<Resource<CustomerProfileResponse>>()
    val customerInfoResponse: MutableLiveData<Resource<CustomerProfileResponse>> get() = _customerInfoResponse
    fun getCustomerInfo(token: String) {
        viewModelScope.launch {
            _customerInfoResponse.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerProfile(token).let {
                _customerInfoResponse.postValue(it)
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