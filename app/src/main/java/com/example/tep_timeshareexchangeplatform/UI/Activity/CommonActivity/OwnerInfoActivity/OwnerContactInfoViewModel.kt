package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.OwnerInfoActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.SentRequestDTO
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerContactInfoViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {

    // Call API to POST Contact Request
    private val postContactRequest = MutableLiveData<Resource<Void>>()
    val postContactRequestResponse: MutableLiveData<Resource<Void>>
        get() = postContactRequest
    fun postContactRequest(token: String, postingId: Int, sentRequestDTO: SentRequestDTO) {
        viewModelScope.launch {
            postContactRequest.postValue(Resource.loading(null))
            customerAPIRepository.sendContactRequest(token, postingId, sentRequestDTO).let {
                postContactRequest.postValue(it)
            }
        }

    }

}