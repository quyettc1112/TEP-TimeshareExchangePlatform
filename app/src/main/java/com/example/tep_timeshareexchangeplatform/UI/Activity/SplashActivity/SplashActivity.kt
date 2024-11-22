package com.example.tep_timeshareexchangeplatform.UI.Activity.SplashActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var bindind: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindind = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bindind.root)

        tokenManager = TokenManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeViewModel()


        // Load the animations
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_scale)

        bindind.logoImageView.startAnimation(logoAnimation)
        checkJwtUserValid()

    }

    private fun observeViewModel() {
        splashViewModel.customerProfileResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    // Do Nothing
                }
                Status.SUCCESS -> {
                    if (response.data!!.isMember) {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                        tokenManager.saveProfileInfo(response.data)
                    }
                    else {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                        tokenManager.saveProfileInfo(response.data)
                    }
                    handlerLooper()
                    Log.d("CheckJwtUserValid", "observeViewModel: ${response.data}")
                }

                Status.ERROR -> {
                    if (response.message?.contains("404") == true) {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_USER)
                        handlerLooper()
                    }
                    Log.d("CheckJwtUserValid", "observeViewModel: ${response.message}")
                }
            }
        }

    }



    private fun handlerLooper() {
       Handler(Looper.getMainLooper()).postDelayed({
           val intent = Intent(this, MainActivity::class.java)
           startActivity(intent)
           finish()
       }, 1000)
    }

    private fun checkJwtUserValid() {
        if (tokenManager.isLoggedIn()) {
            tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_USER)
            splashViewModel.getCustomerProfile(tokenManager.getAccessToken().toString())

        } else {
            tokenManager.saveUserLogState(UserLogState.LOGGED_OUT)
            handlerLooper()
        }
    }
}