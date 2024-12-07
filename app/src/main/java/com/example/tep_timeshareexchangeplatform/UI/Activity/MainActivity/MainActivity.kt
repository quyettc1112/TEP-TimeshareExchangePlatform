package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.SaveTokenDTO
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.AccountFragment.AccountFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment.BookingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.HomeFragment.HomeFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment.PostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.TopResortFragment
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : BaseActivity(), OnBottomNavVisibilityListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var FragmentAdapter: FragmentAdapter
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var tokenManager: TokenManager
    private var FCMToken: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)

        // Check User is logged in or not, member or customer
       // checkUserStateLog()
        changeLangEvent()
        setUpBottomNav()
        observeViewModel()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    private fun observeViewModel() {
        mainViewModel.saveFCMToken.observe(this){
            when(it.status) {
                Status.SUCCESS -> {
                    Log.d("SaveTokenSuccess", it.data.toString())
                }
                Status.ERROR -> {
                    Log.d("SaveTokenSuccess", it.message.toString())
                }
                Status.LOADING -> {
                    Log.d("SaveTokenSuccess", "Loading")
                }
            }
        }

    }


    /**
     * Check User State Log
     * Handler User State Log, Change UI when user is logged in or not
     */
    fun checkUserStateLog() {
        val userLogState = tokenManager.getUserLogState()
        val customerProfileInfo = tokenManager.getProfileInfo()
        when (userLogState) {
            UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                if (customerProfileInfo != null) {
                    mainViewModel.setCustomerInfo(customerProfileInfo)
                }
            }

            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                if (customerProfileInfo != null) {
                    mainViewModel.setCustomerInfo(customerProfileInfo)
                }
            }

            UserLogState.LOGGED_IN_AS_USER -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_IN_AS_USER)
            }

            UserLogState.LOGGED_OUT -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_OUT)
            }
        }
        if (userLogState != UserLogState.LOGGED_OUT) {
            val FCMToken = tokenManager.getFCMToken()
            if (FCMToken != null) {
                callSaveFCMToken(customerProfileInfo!!.id, FCMToken)
            }
        }

    }

    // Check User is logged in or not
    fun checkUserLoggedIn() {
        if (tokenManager.isLoggedIn()) {
            // Decode JWT token to JWTPayloadModel
            val jwtPayloadModel =
                JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken().toString())
            // Save tokens to shared preferences
            jwtPayloadModel?.let { mainViewModel.updateUser(it) }
        }
    }

    private fun callSaveFCMToken(userID: Int, FCMToken: String) {
        val saveTokenDTO = SaveTokenDTO(userID, FCMToken)
        Log.d("SaveTokenSuccess", saveTokenDTO.toString())
        mainViewModel.saveFCMToken(tokenManager.getAccessToken().toString(), saveTokenDTO)
    }


    /**
     * Handle Bottom Navigation Logic
     */
    private fun setUpBottomNav() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(HomeFragment())
        listFragment.add(TopResortFragment())
        listFragment.add(BookingFragment())
        listFragment.add(PostingFragment())
        listFragment.add(AccountFragment())

        FragmentAdapter = FragmentAdapter(this, listFragment)
        binding.vp2Main.apply {
            adapter = FragmentAdapter
            isUserInputEnabled = false
            offscreenPageLimit = 5
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.niceBottomNav.setActiveItem(position)
                    super.onPageSelected(position)
                }
            })
        }
        binding.niceBottomNav.apply {
            setBadge(4)
            onItemSelected = { idFragemnt ->
                binding.vp2Main.setCurrentItem(idFragemnt, true)
            }
        }
        binding.vp2Main.setPageTransformer { page, position ->
            val absPos = Math.abs(position)
            page.apply {
                // Fade effect
                alpha = 1 - absPos

                // Scale effect
                scaleX = 0.85f + (1 - absPos) * 0.15f
                scaleY = 0.85f + (1 - absPos) * 0.15f
            }
        }
    }


    /**
     * Common Event Function
     */
    private fun changeLangEvent() {
        val preferenceHelper = PreferenceHelper(this)
        val savedLanguage = preferenceHelper.getLanguage()
        val locale = savedLanguage?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config = Configuration()
        config.setLocale(locale)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }

    override fun hideBottomNav() {
        binding.cardView.animate().translationY(binding.cardView.height.toFloat()).duration = 30
    }

    override fun showBottomNav() {
        binding.cardView.animate().translationY(0f).duration = 30
    }


    /**
     * LIFE CYCLE
     */
    override fun onResume() {
        super.onResume()

        checkUserLoggedIn()
        checkUserStateLog()
        //mainViewModel.resetCurrentMyBookingPage()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        showConfirmDialog(
            "Thoát ứng dụng",
            "Bạn có chắc chắn muốn thoát ứng dụng không?",
            "Có",
            "Không",
            null,
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {

                }

                override fun positiveAction() {
                    finish()
                }

            }
        )

    }


}