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