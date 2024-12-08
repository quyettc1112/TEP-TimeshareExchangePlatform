package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyDashboardActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DailySummaryDataResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DashboardDataResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyDashboardViewModel @Inject constructor(
    private val customerAPIRepository: CustomerAPIRepository
): ViewModel() {
    
    private val  _customerDashboardResponse = MutableLiveData<Resource<DashboardDataResponse>>()
    val dashboardData: MutableLiveData<Resource<DashboardDataResponse>> get() =  _customerDashboardResponse
    fun getDashboardData(token: String) {
        viewModelScope.launch {
             _customerDashboardResponse.postValue(Resource.loading(null))
            customerAPIRepository.getDashboardData(token).let {
                 _customerDashboardResponse.postValue(it)
            }
        }
    }

    private val  _DailySummaryDataResponse = MutableLiveData<Resource<DailySummaryDataResponse>>()
    val dailySummaryData: MutableLiveData<Resource<DailySummaryDataResponse>> get() =  _DailySummaryDataResponse
    fun getDailySummaryData(token: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            _DailySummaryDataResponse.postValue(Resource.loading(null))
            customerAPIRepository.getDailySummaryData(token, startDate, endDate).let {
                _DailySummaryDataResponse.postValue(it)
            }
        }
    }


}