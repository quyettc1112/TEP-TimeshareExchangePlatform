package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.PostingDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingDetailViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call get Posting Detail API
    private val _postingDetail = MutableLiveData<Resource<PublicPostingDetailResponse>>()
    val postingDetail: MutableLiveData<Resource<PublicPostingDetailResponse>> = _postingDetail
    fun getPostingDetail(postingId: Int) {
        viewModelScope.launch {
            _postingDetail.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostingDetail(postingId).let {
                _postingDetail.postValue(it)
            }
        }
    }

    fun getCurrentPackage(): PackageModel? {
        val rentalPackageEnum =
            RentalPackageEnum.getPackageByName(_postingDetail.value?.data?.rentalPackageName.toString())
        return rentalPackageEnum
    }


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