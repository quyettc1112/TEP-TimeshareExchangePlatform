package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PaymentAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentPackageViewModel @Inject constructor(
    private val paymentAPIRepository: PaymentAPIRepository,
    private val walletAPIRepository: WalletAPIRepository
) : ViewModel() {

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


    // Call API Extend Membership by Wallet
    private val _memberShipResponse = MutableLiveData<Resource<MemberShipResponse>>()
    val memberShipResponse: MutableLiveData<Resource<MemberShipResponse>> get() = _memberShipResponse

    fun extendMembershipByWallet(token: String, membershipId: Int) {
        viewModelScope.launch {
            _memberShipResponse.postValue(Resource.loading(null))
            walletAPIRepository.extendMembershipWallet(token, membershipId).let {
                _memberShipResponse.postValue(it)
            }
        }
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