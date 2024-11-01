package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment

import androidx.lifecycle.ViewModel
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicResortAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopResortViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
    private val publicResortAPIRepository: PublicResortAPIRepository
) : ViewModel() {

    /*// Call API Postings
    private val _postingsResponse = MutableLiveData<Resource<PostingsResponse>>()
    val postingsResponse: MutableLiveData<Resource<PostingsResponse>> get() = _postingsResponse
    fun getPostings(pageNo: Int, pageSize: Int, resortName: String)  {
        viewModelScope.launch {
            _postingsResponse.postValue(Resource.loading(null))
            publicPostingAPIRepository.getPublicPostings(pageNo, pageSize, resortName).let {
                _postingsResponse.postValue(it)
            }
        }
    }*/

}