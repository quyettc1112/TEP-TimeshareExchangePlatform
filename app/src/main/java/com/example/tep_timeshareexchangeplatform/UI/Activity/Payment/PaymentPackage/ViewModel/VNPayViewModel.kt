package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RentalPostingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare.PostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.VNPAYPurchaseResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VNPayViewModel @Inject constructor(
    private val walletAPIRepository: WalletAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call API Purchase Package VNPAY, Extend Membership VNPAY
    private val _memberShipResponse = MutableLiveData<Resource<MemberShipResponse>>()
    val memberShipResponse: MutableLiveData<Resource<MemberShipResponse>> = _memberShipResponse
    fun extendMembership(token: String, uuid: String, membershipId: Int) {
        viewModelScope.launch {
            _memberShipResponse.postValue(Resource.loading(null))
            walletAPIRepository.extendMembershipVNPAY(token, uuid, membershipId).let {
                _memberShipResponse.postValue(it)
            }
        }
    }

    // Call API Deposit to Wallet by VNPAY
    private val _depositByVNPAYResponse = MutableLiveData<Resource<VNPAYPurchaseResponse>>()
    val depositByVNPAYResponse: MutableLiveData<Resource<VNPAYPurchaseResponse>> =
        _depositByVNPAYResponse

    fun depositMoney(token: String, uuid: String) {
        viewModelScope.launch {
            _depositByVNPAYResponse.postValue(Resource.loading(null))
            walletAPIRepository.depositMoneyVNPAY(token, uuid).let {
                _depositByVNPAYResponse.postValue(it)
            }
        }
    }

    // Call API Purchase Package by VNPAY
    private val _purchasePackageResponse = MutableLiveData<Resource<VNPAYPurchaseResponse>>()
    val createRentalPostingTransaction: MutableLiveData<Resource<VNPAYPurchaseResponse>> =
        _purchasePackageResponse

    fun createRentalPostingTransaction(token: String, uuid: String, packageId: Int) {
        viewModelScope.launch {
            _purchasePackageResponse.postValue(Resource.loading(null))
            walletAPIRepository.purchasePackagePostingVNPAY(token, uuid, packageId).let {
                _purchasePackageResponse.postValue(it)
            }
        }
    }

    // Call API Create Posting
    private val _postingTimeshareResponse = MutableLiveData<Resource<PostingTimeshareResponse>>()
    val postingTimeshareResponse: MutableLiveData<Resource<PostingTimeshareResponse>> =
        _postingTimeshareResponse

    fun createRentalPosting(token: String, postingTimeshareResponse: RentalPostingDTO) {
        viewModelScope.launch {
            _postingTimeshareResponse.postValue(Resource.loading(null))
            customerAPIRepository.createPosting(token, postingTimeshareResponse).let {
                _postingTimeshareResponse.postValue(it)
            }
        }
    }

    // Call API Create Booking Transaction
    private val _bookingResponse = MutableLiveData<Resource<VNPAYPurchaseResponse>>()
    val bookingResponse: MutableLiveData<Resource<VNPAYPurchaseResponse>> = _bookingResponse
    fun bookingRentalTransaction(token: String, uuid: String, rentalId: Int) {
        viewModelScope.launch {
            _bookingResponse.postValue(Resource.loading(null))
            walletAPIRepository.bookingRentalVNPAY(token, uuid, rentalId).let {
                _bookingResponse.postValue(it)
            }
        }
    }

    // Call API Create Exchange Posting
    private val _exchangePostingResponse = MutableLiveData<Resource<PostingTimeshareResponse>>()
    val exchangePostingResponse: MutableLiveData<Resource<PostingTimeshareResponse>> =
        _exchangePostingResponse
    fun createExchangePosting(token: String, postingTimeshareResponse: ExchangePostingDTO) {
        viewModelScope.launch {
            _exchangePostingResponse.postValue(Resource.loading(null))
            customerAPIRepository.createExchangePosting(token, postingTimeshareResponse).let {
                _exchangePostingResponse.postValue(it)
            }
        }
    }

    // Create Exchange Posting Transcation
    private val _exchangePostingResponseTransaction = MutableLiveData<Resource<VNPAYPurchaseResponse>>()
    val createExchangePostingTransaction: MutableLiveData<Resource<VNPAYPurchaseResponse>> = _exchangePostingResponseTransaction
    fun createExchangePostingTransaction(token: String, uuid: String, exchangeId: Int) {
        viewModelScope.launch {
            _exchangePostingResponseTransaction.postValue(Resource.loading(null))
            walletAPIRepository.createExchangePostingTransaction(token, uuid, exchangeId).let {
                _exchangePostingResponseTransaction.postValue(it)
            }
        }
    }



}