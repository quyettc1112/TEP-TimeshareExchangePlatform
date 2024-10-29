package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
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
    val purchasePackageResponse: MutableLiveData<Resource<VNPAYPurchaseResponse>> =
        _purchasePackageResponse

    fun purchasePackage(token: String, uuid: String, packageId: Int) {
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

    fun createPosting(token: String, postingTimeshareResponse: PostingTimeshareDTO) {
        viewModelScope.launch {
            _postingTimeshareResponse.postValue(Resource.loading(null))
            customerAPIRepository.createPosting(token, postingTimeshareResponse).let {
                _postingTimeshareResponse.postValue(it)
            }
        }
    }


}