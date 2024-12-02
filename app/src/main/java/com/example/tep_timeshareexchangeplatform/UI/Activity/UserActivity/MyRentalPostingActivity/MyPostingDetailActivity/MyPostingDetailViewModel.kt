package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.StorageAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MyPostingDetailViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository,
    private val storageAPIRepository: StorageAPIRepository
) : ViewModel(){

    // Call My posting detail
    private val _postingDetailResponse = MutableLiveData<Resource<MyRentalPostingDetailResponse>>()
    val postingDetailResponse: MutableLiveData<Resource<MyRentalPostingDetailResponse>>
        get() = _postingDetailResponse
    fun getMyPostingDetail(token: String, postingId: Int) {
        viewModelScope.launch {
            _postingDetailResponse.postValue(Resource.loading(null))
            customerAPIRepository.getMyPostingDetail(token, postingId).let {
                _postingDetailResponse.postValue(it)
            }
        }
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
    }
}