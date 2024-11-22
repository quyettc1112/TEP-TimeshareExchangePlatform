package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.StorageAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.UserJWTPayloadModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository,
    private val storageAPIRepository: StorageAPIRepository
): ViewModel() {

    private val _customerInfo = MutableLiveData<Resource<CustomerInfoResponse>>()
    val customerInfo: MutableLiveData<Resource<CustomerInfoResponse>> = _customerInfo
    fun getCustomerInfo(token: String) {
        viewModelScope.launch {
            _customerInfo.value = Resource.loading(null)
            customerAPIRepository.getIsCustomerExist(token).let {
                _customerInfo.value = it
            }
        }
    }

    /**
     * Tracking User Login State
     *
     * This function is responsible for tracking the user's login state.
     */
    private val _userJWTPayload = MutableLiveData<UserJWTPayloadModel>()
    val userJWTPayload: LiveData<UserJWTPayloadModel> = _userJWTPayload

    // Tracking User Login State
    private val _userLogState = MutableLiveData<UserLogState>()
    val userLogState: LiveData<UserLogState> = _userLogState
    fun setUserLogState(state: UserLogState) {
        _userLogState.value = state
    }


    // Get Customer Profile
    private val _customerProfile = MutableLiveData<Resource<CustomerProfileResponse>>()
    val customerProfile: LiveData<Resource<CustomerProfileResponse>> = _customerProfile
    fun getCustomerProfile(token: String) {
        viewModelScope.launch {
            _customerProfile.value = Resource.loading(null)
            customerAPIRepository.getCustomerProfile(token).let {
                _customerProfile.value = it
            }
        }
    }

    // Update Customer Profile
    private val _updateCustomerProfile = MutableLiveData<Resource<CustomerProfileResponse>>()
    val updateCustomerProfile: LiveData<Resource<CustomerProfileResponse>> = _updateCustomerProfile
    fun updateCustomerProfile(token: String, profileDTO: ProfileDTO) {
        viewModelScope.launch {
            _updateCustomerProfile.value = Resource.loading(null)
            customerAPIRepository.updateCustomerProfile(token, profileDTO).let {
                _updateCustomerProfile.value = it
            }
        }
    }

    private val _imageList = MutableLiveData<List<ImageUploadModel>>(emptyList())
    val imageList: LiveData<List<ImageUploadModel>> get() = _imageList

    // Đặt ảnh chính
    fun setMainImage(mainImage: ImageUploadModel) {
        _imageList.value = _imageList.value?.toMutableList()?.apply {
            // Kiểm tra nếu ảnh chính đã tồn tại, xóa nó
            remove(mainImage)
            // Thêm ảnh chính vào đầu danh sách
            add(0, mainImage)
        } ?: listOf(mainImage) // Nếu danh sách rỗng, khởi tạo với ảnh chính
    }

    fun getMultipartBodies(): List<MultipartBody.Part> {
        return _imageList.value?.map { it.part } ?: emptyList()
    }
    private val _listImageResponse = MutableLiveData<Resource<List<String>>>()
    val uploadImageResponse: LiveData<Resource<List<String>>> get() = _listImageResponse
    fun callUploadImages(token: String) {
        viewModelScope.launch {
            _listImageResponse.postValue(Resource.loading(null))
            val images = getMultipartBodies()
            val response = storageAPIRepository.uploadFiles(token, images)
            _listImageResponse.postValue(response)
        }
    }



}