package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.AuthAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.LoginResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authAPIRepository: AuthAPIRepository
): ViewModel() {

    // Login ViewModel Tracking
    private val _loginResponse = MutableLiveData<Resource<LoginResponse>>()
    val loginResponse: LiveData<Resource<LoginResponse>> get() = _loginResponse
    // Login Function
    fun login(loginDTO: LoginDTO) {
        viewModelScope.launch {
            _loginResponse.postValue(Resource.loading(null))
            val result = authAPIRepository.login(loginDTO)
            _loginResponse.postValue(result)
        }
    }


}