package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostingViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {



    // Call My Posting Detail API
    private val _myPostingList = MutableLiveData<Resource<MyPostingResponse>>()
    val myPostingList: MutableLiveData<Resource<MyPostingResponse>>
        get() = _myPostingList
    fun getMyPostingList(token: String) {
        viewModelScope.launch {
            _myPostingList.postValue(Resource.loading(null))
            customerAPIRepository.getMyPostingList(token).let {
                _myPostingList.postValue(it)
            }
        }
    }

}
