package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.StorageAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RentalPostingUpdateDTO
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
    fun getImageList(): List<String> {
        return _postingDetailResponse.value?.data?.imageUrls ?: emptyList()
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
    // Update Rental Posting
    private val _updateRentalResponse = MutableLiveData<Resource<Void>?>()
    val updateRentalResponse: LiveData<Resource<Void>?> get() = _updateRentalResponse
    fun callUpdateRentalPosting(token: String, postingId: Int, rentalPostingUpdateDTO: RentalPostingUpdateDTO) {
        viewModelScope.launch {
            _updateRentalResponse.postValue(Resource.loading(null))
            val response = customerAPIRepository.updateRentalPosting(token, postingId, rentalPostingUpdateDTO)
            _updateRentalResponse.postValue(response)
        }
    }

    fun resetUpdateRentalResponse() {
        _updateRentalResponse.value = null
        _listImageResponse.value = null
        _listImageForPut.value = emptyList()
        _imageListFromDevice.value = emptyList()

    }



    // Cancel Policy
    private val _cancelPolicy = MutableLiveData<Int>()
    val cancelPolicy: MutableLiveData<Int>
        get() = _cancelPolicy

    fun updateCancelPolicy(cancelPolicy: Int) {
        _cancelPolicy.value = cancelPolicy
    }

    // Tracking PricePerNight
    private val _pricePerNight = MutableLiveData<Long>()
    val pricePerNight: MutableLiveData<Long>
        get() = _pricePerNight

    fun updatePricePerNight(pricePerNight: Long) {
        _pricePerNight.value = pricePerNight
    }

}