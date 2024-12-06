package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.StorageAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MyExchangeDetailViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository,
    private val storageAPIRepository: StorageAPIRepository
): ViewModel() {

    // Get My Exchange Detail
    private val _myExchangeDetail = MutableLiveData<Resource<MyExchangePostingDetailResponse>>()
    val myExchangeDetail: MutableLiveData<Resource<MyExchangePostingDetailResponse>> = _myExchangeDetail
    fun getCustomerExchangeDetail(token: String, id: Int) {
        viewModelScope.launch {
            _myExchangeDetail.postValue(Resource.loading(null))
            customerAPIRepository.getCustomerExchangePostingDetail(token, id).let {
                _myExchangeDetail.postValue(it)
            }
        }
    }
    fun getImageList(): List<String> {
        return _myExchangeDetail.value?.data?.imageUrls ?: emptyList()
    }



    // Currnt Image List. For Put
    private val _listImageForPut =  MutableLiveData<List<String>?>(emptyList())
    val listImageForPut: LiveData<List<String>?> get() = _listImageForPut
    fun getImagesForPut(): List<String>? {
        return _listImageForPut.value ?: emptyList()
    }
    fun addListImageForPut(newImages: List<String>? = null) {
        _listImageForPut.value = _listImageForPut.value?.toMutableList()?.apply {
            addAll(newImages?: emptyList())
        }
    }
    fun deleteImageForPut(image: String) {
        _listImageForPut.value = _listImageForPut.value?.toMutableList()?.apply {
            remove(image)
        }
    }
    fun clearListImageForPut() {
        _listImageForPut.value = emptyList()
    }



    private val _imageListFromDevice = MutableLiveData<List<ImageUploadModel>>(emptyList())
    fun addImagesFromDevice(newImages: List<ImageUploadModel>) {
        _imageListFromDevice.value = _imageListFromDevice.value?.toMutableList()?.apply {
            addAll(newImages)
        }
    }
    fun getMultipartBodies(): List<MultipartBody.Part> {
        return _imageListFromDevice.value?.map { it.part } ?: emptyList()
    }

    private val _listImageResponse = MutableLiveData<Resource<List<String>>?>()
    val uploadImageResponse: LiveData<Resource<List<String>>?> get() = _listImageResponse
    fun callUploadImages(token: String) {
        viewModelScope.launch {
            _listImageResponse.postValue(Resource.loading(null))
            val images = getMultipartBodies()
            val response = storageAPIRepository.uploadFiles(token, images)
            _listImageResponse.postValue(response)
        }
    }

    fun getUploadedImageUrls(): List<String> {
        return _listImageResponse.value?.data ?: emptyList()
    }


    // Update Exchange Posting
    private val _updateExchangeResponse = MutableLiveData<Resource<Void>?>()
    val updateExchangeResponse: LiveData<Resource<Void>?> get() = _updateExchangeResponse
    fun callUpdateExchangePosting(token: String, postingId: Int, exchangePostingUpdateDTO: ExchangePostingUpdateDTO) {
        viewModelScope.launch {
            _updateExchangeResponse.postValue(Resource.loading(null))
            val response = customerAPIRepository.updateExchangePosting(token, postingId, exchangePostingUpdateDTO)
            _updateExchangeResponse.postValue(response)
        }
    }


    fun resetUpdateExchangeResponse() {
        _updateExchangeResponse.value = null
        _listImageResponse.value = null
        _listImageForPut.value = emptyList()
        _imageListFromDevice.value = emptyList()

    }


}