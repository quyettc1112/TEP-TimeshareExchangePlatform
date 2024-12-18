package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.NotificationAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotiUpdateRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotificationResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationAPIRepository: NotificationAPIRepository
): ViewModel(){
    // Call Get Notification API
    private val _notificationResponse = MutableLiveData<Resource<NotificationResponse>>()
    val notificationResponse: MutableLiveData<Resource<NotificationResponse>> = _notificationResponse
    fun callGetNotificationAPI(token: String, pageNo: Int, pageSize: Int){
        viewModelScope.launch {
            _notificationResponse.postValue(Resource.loading(null))
            notificationAPIRepository.getCustomerNotification(token, pageNo, pageSize).let {
                _notificationResponse.postValue(it)
            }
        }
    }
    private val _currentNotificationList = MutableLiveData<List<NotificationResponse.Content>>()
    fun loadMoreNotifications(list: List<NotificationResponse.Content>) {
        val currentList = _currentNotificationList.value ?: emptyList()
        val updatedList = currentList + list
        _currentNotificationList.value = updatedList
    }

    fun getCurrentNotificationList(): List<NotificationResponse.Content>? {
        return _currentNotificationList.value
    }

    private val _currentNotificationPage = MutableLiveData<Int>()
    var currentNotificationPage: MutableLiveData<Int> = _currentNotificationPage
    fun getCurrentNotificationPage(): Int {
        return _currentNotificationPage.value ?: 0
    }

    fun incrementCurrentNotificationPage() {
        val currentValue = _currentNotificationPage.value ?: 0
        _currentNotificationPage.value = currentValue + 1
    }
    fun clearCurrentNotificationList() {
        _currentNotificationList.value = emptyList()
    }

    // Call Mark Read API
    private val _markReadResponse = MutableLiveData<Resource<NotiUpdateRespone>>()
    val markReadResponse: MutableLiveData<Resource<NotiUpdateRespone>> = _markReadResponse
    fun callMarkReadAPI(token: String, notiId: Int){
        viewModelScope.launch {
            _markReadResponse.postValue(Resource.loading(null))
            notificationAPIRepository.markReadNotification(token, notiId).let {
                _markReadResponse.postValue(it)
            }
        }
    }


    // Call Mark All Read API
    private val _markAllReadResponse = MutableLiveData<Resource<Boolean>>()
    val markAllReadResponse: MutableLiveData<Resource<Boolean>> = _markAllReadResponse
    fun callMarkAllReadAPI(token: String, userId: Int){
        viewModelScope.launch {
            _markAllReadResponse.postValue(Resource.loading(null))
            notificationAPIRepository.markAllReadNotification(token, userId).let {
                _markAllReadResponse.postValue(it)
            }
        }
    }
}