package com.example.tep_timeshareexchangeplatform.UI.Activity.SplashActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private lateinit var bindind: ActivitySplashBinding

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


        // Load the animations
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_scale)
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.text_fade_in)

        bindind.logoImageView.startAnimation(logoAnimation)

        // Delay before navigating to the next screen
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 3000) // Adjust delay as needed
    }
}