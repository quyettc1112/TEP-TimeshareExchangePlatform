package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment

import androidx.lifecycle.ViewModel
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PublicPostingViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository,
) : ViewModel() {
    // Call API Postings

}