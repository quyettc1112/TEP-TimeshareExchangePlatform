package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.AuthAPIRepository
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authAPIRepository: AuthAPIRepository
): ViewModel() {
    private val _viewPagerPosition = MutableLiveData<Int>()
    val viewPagerPosition: LiveData<Int> = _viewPagerPosition

    // Hàm để cập nhật vị trí
    fun setViewPagerPosition(position: Int) {
        _viewPagerPosition.value = position
    }




    private val _email = MutableLiveData<String>()
    val email: MutableLiveData<String>
        get() = _email
    fun setEmail(email: String) {
        _email.value = email
    }


    private val _newPassword = MutableLiveData<String>()
    val newPassword: MutableLiveData<String>
        get() = _newPassword
    fun setNewPassword(newPassword: String) {
        _newPassword.value = newPassword
    }

    private val _token = MutableLiveData<String>()
    val token: MutableLiveData<String>
        get() = _token
    fun setToken(token: String) {
        _token.value = token
    }


    init {
        _token.value = ""
        _email.value = ""
        _newPassword.value = ""
    }

    // Call Send Email Forgot Password API
    private val _forgotPasswordResponse = MutableLiveData<Resource<Void>>()
    val forgotPasswordResponse: MutableLiveData<Resource<Void>>
        get() = _forgotPasswordResponse
    fun callSendEmailForgotPassword(email: String) {
        viewModelScope.launch {
            _forgotPasswordResponse.postValue(Resource.loading(null))
            authAPIRepository.forgotPassword(email).let {
                _forgotPasswordResponse.postValue(it)
            }
        }
    }

    // Reset Password
    private val _resetPasswordResponse = MutableLiveData<Resource<Void>>()
    val resetPasswordResponse: MutableLiveData<Resource<Void>>
        get() = _resetPasswordResponse
    fun callResetPassword(email: String, token: String, newPassword: String) {
        viewModelScope.launch {
            _resetPasswordResponse.postValue(Resource.loading(null))
            authAPIRepository.resetPassword(email, token, newPassword).let {
                _resetPasswordResponse.postValue(it)
            }
        }
    }

}