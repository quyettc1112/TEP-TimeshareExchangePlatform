package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.PricingSupportActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.PricingSupportResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PricingSupportViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
) : ViewModel() {

    // Call API Accept / Reject Pricing Support
    private val _pricingSupportResponse = MutableLiveData<Resource<PricingSupportResponse>>()
    val pricingSupportResponse: MutableLiveData<Resource<PricingSupportResponse>> =
        _pricingSupportResponse

    fun acceptPricingSupport(token: String, timeShareId: Int, newPrice: Float, isAccept: Boolean?) {
        viewModelScope.launch {
            _pricingSupportResponse.postValue(Resource.loading(null))
            customerAPIRepository.acceptPriceSupport(token, timeShareId, newPrice, isAccept).let {
                _pricingSupportResponse.postValue(it)
            }
        }
    }

    private val _pricePerNight = MutableLiveData<Long>()
    val pricePerNight: MutableLiveData<Long>
        get() = _pricePerNight

    fun updatePricePerNight(pricePerNight: Long) {
        _pricePerNight.value = pricePerNight
    }


}