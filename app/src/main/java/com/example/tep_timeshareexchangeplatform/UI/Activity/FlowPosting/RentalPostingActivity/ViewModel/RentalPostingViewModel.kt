package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RentalPostingViewModel: ViewModel() {
    private val _step = MutableLiveData<Int>()
    val step: MutableLiveData<Int>
        get() = _step

    init {
        _step.value = 1
    }

    // Funtion to update step
    fun updateStep(step: Int){
        _step.value = step
    }


}