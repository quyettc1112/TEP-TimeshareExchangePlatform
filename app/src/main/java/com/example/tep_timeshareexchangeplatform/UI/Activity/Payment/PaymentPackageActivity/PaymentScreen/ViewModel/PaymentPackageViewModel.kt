package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.ViewModel

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
class PaymentPackageViewModel @Inject constructor(
    private val paymentAPIRepository: PaymentAPIRepository
) : ViewModel() {

    // init MutableLiveData
    private val _responseUrl = MutableLiveData<Resource<PaymentResponse>>()
    val responseUrl: MutableLiveData<Resource<PaymentResponse>> = _responseUrl

    // call API to get response URL
    fun getResponsePaymentUrl(amount: Int, orderType: String) {
        viewModelScope.launch {
            _responseUrl.postValue(Resource.loading(null))
            paymentAPIRepository.getPaymentUrl(amount, orderType).let {
                _responseUrl.postValue(it)
            }
        }
    }
}