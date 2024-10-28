package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    /*// Call API Get Is Customer Exist
    private val _isCustomerExist = MutableLiveData<Resource<CustomerResponse>>()
    val isCustomerExist: MutableLiveData<Resource<CustomerResponse>>
        get() = _isCustomerExist
    fun callIsCustomerExist(token: String, userId: Int) {
        viewModelScope.launch {
            _isCustomerExist.postValue(Resource.loading(null))
            customerAPIRepository.getIsCustomerExist(token, userId).let {
                _isCustomerExist.postValue(it)
            }
        }
    }*/

}