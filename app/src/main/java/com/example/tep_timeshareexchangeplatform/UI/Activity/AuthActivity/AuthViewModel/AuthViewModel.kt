package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.AuthAPIRepository
import com.example.tep_timeshareexchangeplatform.API.Repository.CustomerAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RegisterDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.LoginResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.RegisterResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authAPIRepository: AuthAPIRepository,
    private val customerAPIRepository: CustomerAPIRepository
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

    // Register ViewModel Tracking
    private val _registerResponse = MutableLiveData<Resource<RegisterResponse>>()
    val registerResponse: LiveData<Resource<RegisterResponse>> get() = _registerResponse
    // Call Register Function
    fun register(registerDTO: RegisterDTO) {
        viewModelScope.launch {
            _registerResponse.postValue(Resource.loading(null))
            val result = authAPIRepository.register(registerDTO)
            _registerResponse.postValue(result)
        }
    }

    // Check if customer exist, customer Info
    private val _customerInfoResponse = MutableLiveData<Resource<CustomerInfoResponse>>()
    val customerInfoResponse: LiveData<Resource<CustomerInfoResponse>> get() = _customerInfoResponse
    // Call Check if customer exist, customer Info Function
    fun getIsCustomerExist(token: String) {
        viewModelScope.launch {
            _customerInfoResponse.postValue(Resource.loading(null))
            val result = customerAPIRepository.getIsCustomerExist(token)
            _customerInfoResponse.postValue(result)
        }
    }



}