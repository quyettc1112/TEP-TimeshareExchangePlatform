package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VNPayViewModel @Inject constructor(
    private val walletAPIRepository: WalletAPIRepository
) : ViewModel() {

    // Call API Extend Membership
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
}