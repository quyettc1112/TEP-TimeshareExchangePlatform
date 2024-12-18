package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PolicyActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.FAQ.FAQResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Policy.PolicyResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
) : ViewModel(){
    // Call get policy API
    private val _policyResponse = MutableLiveData<Resource<PolicyResponse>>()
    val policyResponse: MutableLiveData<Resource<PolicyResponse>>
        get() = _policyResponse

    fun callGetAllPolicy() {
        viewModelScope.launch {
            _policyResponse.postValue(Resource.loading(null))
            publicPostingAPIRepository.getAllPolicy().let {
                _policyResponse.postValue(it)
            }
        }
    }

    // Call Get FAQ
    private val _faqResponse = MutableLiveData<Resource<FAQResponse>>()
    val faqResponse: MutableLiveData<Resource<FAQResponse>>
        get() = _faqResponse
    fun callGetAllFAQ() {
        viewModelScope.launch {
            _faqResponse.postValue(Resource.loading(null))
            publicPostingAPIRepository.getFAQ().let {
                _faqResponse.postValue(it)
            }
        }
    }
}