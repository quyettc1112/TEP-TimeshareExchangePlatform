package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.MemberShipActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum

class MemberShipViewModel : ViewModel() {

    // Current Package Selected
    var _currentPackage = MutableLiveData<PackageModel>()
    val currentPackage: MutableLiveData<PackageModel>
        get() = _currentPackage

    // Update Current Package
    fun updateCurrentPackage(packageModel: PackageModel) {
        _currentPackage.value = packageModel
    }




}