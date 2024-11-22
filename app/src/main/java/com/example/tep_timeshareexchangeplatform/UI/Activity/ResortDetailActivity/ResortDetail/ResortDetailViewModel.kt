package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResortDetailViewModel @Inject constructor(
    private val publicResortAPIRepository: PublicResortAPIRepository,

) : ViewModel() {

    private val _resortDetail = MutableLiveData<Resource<ResortDetailModelResponse>>()
    val resortDetail: MutableLiveData<Resource<ResortDetailModelResponse>> = _resortDetail

    fun getResortDetail(resortId: Int) {
        viewModelScope.launch {
            _resortDetail.postValue(Resource.loading(null))
            publicResortAPIRepository.getResortDetail(resortId).let {
                _resortDetail.postValue(it)
            }
        }
    }




}