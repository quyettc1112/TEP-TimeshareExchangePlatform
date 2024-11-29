package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangesResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPostingViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call Get Public Posting API
    private val _publicRentalPosingList = MutableLiveData<Resource<PublicPostingResponse>>()
    val publicRentalPosingList: MutableLiveData<Resource<PublicPostingResponse>> get() = _publicRentalPosingList
    fun getRentalPostingList(pageNo: Int, pageSize: Int, name: String) {
        viewModelScope.launch {
            _publicRentalPosingList.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, name).let {
                _publicRentalPosingList.postValue(it)
            }
        }
    }

    private val _currentPostingList = MutableLiveData<List<PublicPostingResponse.Content>>()
    fun loadMorePostings(list: List<PublicPostingResponse.Content>) {
        val currentList = _currentPostingList.value ?: emptyList()
        val updatedList = currentList + list
        _currentPostingList.value = updatedList
    }

    fun getCurrentPostingList(): List<PublicPostingResponse.Content>? {
        return _currentPostingList.value
    }

    private val _currentPostingsPage = MutableLiveData<Int>()
    var currentPostingsPage: LiveData<Int> = _currentPostingsPage
    fun getCurrentPostingsPage(): Int {
        return _currentPostingsPage.value ?: 0
    }

    fun incrementCurrentPostingsPage() {
        val currentValue = _currentPostingsPage.value ?: 0
        _currentPostingsPage.value = currentValue + 1
    }

    // Call Get Public Exchange Posting API
    private val _publicExchangePosingList = MutableLiveData<Resource<ExchangesResponse>>()
    val publicExchangePosingList: MutableLiveData<Resource<ExchangesResponse>> get() = _publicExchangePosingList

    fun getExchangePostingList(pageNo: Int, pageSize: Int, name: String) {
        viewModelScope.launch {
            _publicExchangePosingList.postValue(Resource.loading(null))
            publicPostingAPIRepository.getExchangePostings(pageNo, pageSize, name).let {
                _publicExchangePosingList.postValue(it)
            }
        }
    }

    private val _currentExchangeList = MutableLiveData<List<ExchangesResponse.Content>>()
    fun loadMoreExchange(list: List<ExchangesResponse.Content>) {
        val currentList = _currentExchangeList.value ?: emptyList()
        val updatedList = currentList + list
        _currentExchangeList.value = updatedList
    }

    fun getCurrentExchangeList(): List<ExchangesResponse.Content>? {
        return _currentExchangeList.value
    }

    private val _currentExchangePage = MutableLiveData<Int>()
    var currentExchangePage: LiveData<Int> = _currentExchangePage
    fun getCurrentExchangePage(): Int {
        return _currentExchangePage.value ?: 0
    }

    fun incrementCurrentExchangePage() {
        val currentValue = _currentExchangePage.value ?: 0
        _currentExchangePage.value = currentValue + 1
    }


    init {
        _currentPostingsPage.value = 0
        _currentExchangePage.value = 0
    }

    // Call get Is Member API
    // Call API Get Is Customer Exist
    private val _isCustomerExist = MutableLiveData<Resource<CustomerProfileResponse>>()
    val isCustomerExist: MutableLiveData<Resource<CustomerProfileResponse>>
        get() = _isCustomerExist

    fun callIsCustomerExist(token: String) {
        viewModelScope.launch {
            _isCustomerExist.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerProfile(token).let {
                _isCustomerExist.postValue(it)
            }
        }
    }

    // Call API Create Customer
    private val _customerResponse = MutableLiveData<Resource<CustomerResponse>>()
    val createCustomerResponse: MutableLiveData<Resource<CustomerResponse>>
        get() = _customerResponse

    fun callCreateCustomer(token: String, customerDTO: CustomerDTO) {
        viewModelScope.launch {
            _customerResponse.postValue(Resource.loading(null))
            customerAPIRepository.createCustomer(token, customerDTO).let {
                _customerResponse.postValue(it)
            }
        }
    }


}