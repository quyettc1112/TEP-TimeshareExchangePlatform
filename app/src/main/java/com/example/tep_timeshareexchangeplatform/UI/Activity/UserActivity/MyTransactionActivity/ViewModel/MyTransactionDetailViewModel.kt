package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDetailRespone
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTransactionDetailViewModel @Inject constructor(
    private val walletAPIRepository: WalletAPIRepository
) : ViewModel() {

    // Call API to get transaction detail
    private val _walletDetailResponse = MutableLiveData<Resource<WalletDetailRespone>>()
    val walletDetailResponse: MutableLiveData<Resource<WalletDetailRespone>> = _walletDetailResponse

    fun getWalletDetail(token: String, transactionId: String) {
        viewModelScope.launch {
            _walletDetailResponse.postValue(Resource.loading(null))
            walletAPIRepository.getWalletTransactionDetailByUUID(token, transactionId).let {
                _walletDetailResponse.postValue(it)
            }
        }
    }
}