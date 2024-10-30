package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.TimeshareFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeshareViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
) : ViewModel() {
    // Call API Postings

}