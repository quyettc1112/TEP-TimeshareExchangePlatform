package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.ResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResortDetailViewModel @Inject constructor(
    private val resortAPIRepository: ResortAPIRepository
) : ViewModel() {

    private val _resortDetail = MutableLiveData<Resource<ResortDetailModelResponse>>()
    val resortDetail: MutableLiveData<Resource<ResortDetailModelResponse>> = _resortDetail

    fun getResortDetail(resortId: Int) {
        viewModelScope.launch {
            _resortDetail.postValue(Resource.loading(null))
            resortAPIRepository.getResortDetail(resortId).let {
                _resortDetail.postValue(it)
            }
        }
    }


}