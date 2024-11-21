package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionSpentFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.WalletAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletListResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpentViewModel @Inject constructor(
    private val walletAPIRepository: WalletAPIRepository
) : ViewModel() {
    // Call Get List of Transaction API
    private val _walletListResponse = MutableLiveData<Resource<WalletListResponse>>()
    val walletListResponse: MutableLiveData<Resource<WalletListResponse>> = _walletListResponse
    fun getWalletList(token: String, page: Int, size: Int) {
        viewModelScope.launch {
            _walletListResponse.postValue(Resource.loading(null))
            walletAPIRepository.getSpentTransaction(token,page, size).let {
                _walletListResponse.postValue(it)
            }
        }
    }
    // Check Current Wallet Page
    private var _currentWalletPage = MutableLiveData<Int>()
    val currentWalletPage: MutableLiveData<Int>
        get() = _currentWalletPage
    fun incrementCurrentWalletsPage() {
        val currentValue = _currentWalletPage.value ?: 0
        _currentWalletPage.value = currentValue + 1
    }

    private val _currentWalletList = mutableListOf<WalletListResponse.Content>()
    fun loadMoreWalletList(list: List<WalletListResponse.Content>) {
        _currentWalletList.addAll(list)
    }
    fun getCurrentWalletList(): List<WalletListResponse.Content> {
        return _currentWalletList
    }

    fun clearCurrentWalletList() {
        _currentWalletList.clear()
    }

    init {
        _currentWalletPage.value = 0
    }
}