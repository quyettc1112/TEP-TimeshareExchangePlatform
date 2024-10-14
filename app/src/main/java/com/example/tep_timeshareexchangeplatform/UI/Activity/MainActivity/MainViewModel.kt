package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.AuthAPIRepository.AuthAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.UserRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.User
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.UserJWTPayloadModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.LoginResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel  @Inject constructor(
    private val authAPIRepository: AuthAPIRepository
) : ViewModel()  {

    private val _user = MutableLiveData<UserJWTPayloadModel>()
    val user: LiveData<UserJWTPayloadModel> = _user

    private val _roomCount = MutableLiveData(1)
    val roomCount: LiveData<Int> = _roomCount

    private val _adultCount = MutableLiveData(1)
    val adultCount: LiveData<Int> = _adultCount

    private val _childrenCount = MutableLiveData(0)
    val childrenCount: LiveData<Int> = _childrenCount

    fun updateRoomCount(count: Int) {
        _roomCount.value = count
    }

    fun updateAdultCount(count: Int) {
        _adultCount.value = count
    }

    fun updateChildrenCount(count: Int) {
        _childrenCount.value = count
    }

    fun getRoomCount(): String {
        return "${_adultCount.value} Người lớn, ${_childrenCount.value} Trẻ em, ${_roomCount.value} Phòng"
    }

    fun updateUser(userJWTPayloadModel: UserJWTPayloadModel) {
        userJWTPayloadModel.let {
            _user.value = it
        }
    }






}