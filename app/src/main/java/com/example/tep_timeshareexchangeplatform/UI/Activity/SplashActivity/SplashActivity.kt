package com.example.tep_timeshareexchangeplatform.UI.Activity.SplashActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindind = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bindind.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeViewModel()


        // Load the animations
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_scale)
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.text_fade_in)

        bindind.logoImageView.startAnimation(logoAnimation)
        checkJwtUserValid()

    }

    private fun observeViewModel() {
        splashViewModel.customerResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    // Do Nothing
                }

                Status.SUCCESS -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(Constant.USER_LOGIN_STATE,  UserLogState.LOGGED_IN_AS_CUSTOMER)
                    intent.putExtra(Constant.CUSTOMER_OBJECT, response.data)
                    startActivity(intent)
                }

                Status.ERROR -> {
                    if(response.message?.contains("404") == true) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra(Constant.USER_LOGIN_STATE,  UserLogState.LOGGED_IN_AS_USER)
                        startActivity(intent)
                    }
                }
            }
        }

    }


    private fun checkJwtUserValid() {
        val tokenManager = TokenManager(this)

        // TODO: Check Is Login and Non Expired JWT Token
        if (tokenManager.isLoggedIn()) {
            val userJWTPayloadModel =
                JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken().toString())
            // Call check customer exist API
            splashViewModel.getIsCustomerExist(tokenManager.getAccessToken().toString(), userJWTPayloadModel!!.userId)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constant.USER_LOGIN_STATE,  UserLogState.LOGGED_OUT)
            startActivity(intent)
        }
    }
}