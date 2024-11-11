package com.example.tep_timeshareexchangeplatform.Sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.SampleAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Sample.UserSampleModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val sampleRepository: SampleAPIRepository
): ViewModel() {

    // Khai Báo biến lưu trữ dữ liệu trả về từ API
    private val _userSamepleResponse = MutableLiveData<Resource<UserSampleModel>>()
    val userSampleResponse: MutableLiveData<Resource<UserSampleModel>> = _userSamepleResponse
    // Gọi API để lấy danh sách user

    fun getUserList() {
        _userSamepleResponse.postValue(Resource.loading(null))
        viewModelScope.launch {
            _userSamepleResponse.postValue(Resource.loading(null))
            sampleRepository.getUserList().let {
                _userSamepleResponse.postValue(it)
            }
        }
    }

}