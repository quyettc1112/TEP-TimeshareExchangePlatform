package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.MemberShipActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberShipViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Current Package Selected
    private var _currentPackage = MutableLiveData<PackageModel>()
    val currentPackage: MutableLiveData<PackageModel>
        get() = _currentPackage

    // Update Current Package
    fun updateCurrentPackage(packageModel: PackageModel) {
        _currentPackage.value = packageModel
    }

    // Call API Create Customer
    private val _customerResponse = MutableLiveData<Resource<CustomerResponse>>()
    val customerResponse: MutableLiveData<Resource<CustomerResponse>>
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