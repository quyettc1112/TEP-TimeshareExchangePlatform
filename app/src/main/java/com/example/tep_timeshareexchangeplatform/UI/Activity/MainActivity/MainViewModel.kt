package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.User_Repository
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.User
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel  @Inject constructor(
    private val userRepository: User_Repository
) : ViewModel()  {

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



    private val _currentUserID = MutableLiveData<Int>()
    val currentUserID: LiveData<Int> get() = _currentUserID

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> get() = _user

    fun fetchUserById(userId: Int) {
        viewModelScope.launch {
            _user.postValue(Resource.loading(null))
            val result = userRepository.getUser(userId)
            _user.postValue(result)
        }
    }

    // Hàm này sẽ tăng currentUserID lên 1 khi được gọi
    fun incrementUserID() {
        _currentUserID.value = (_currentUserID.value ?: 0) + 1
    }

}