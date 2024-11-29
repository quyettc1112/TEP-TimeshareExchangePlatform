package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.DepositActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PaymentAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositViewModel  @Inject constructor(
    private val paymentAPIRepository: PaymentAPIRepository,
): ViewModel() {

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

}