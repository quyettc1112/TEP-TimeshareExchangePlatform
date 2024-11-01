package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

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
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.AccountFragment.AccountFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.ExchangeFragment.ExchangeFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.HomeFragment.HomeFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment.PostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.TopResortFragment
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : BaseActivity(), OnBottomNavVisibilityListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var FragmentAdapter: FragmentAdapter

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    companion object {
        const val PAGE_SIZE = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        callGetAPI()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)

        // Check User is logged in or not, member or customer
        checkUserStateLog()
        changeLangEvent()
        setUpBottomNav()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    /**
     * Call API
     */
    private fun callGetAPI() {

        // Call API Resort for Top Resort
        mainViewModel.getResortONTopResort(0, PAGE_SIZE, "")

        // Call API Public Posting for Top Resort
        mainViewModel.getPostingOnTopResort(0, PAGE_SIZE, "")


    }


    /**
     * Check User State Log
     * Handler User State Log, Change UI when user is logged in or not
     */
    private fun checkUserStateLog() {
        val userLogState = tokenManager.getUserLogState()
        val customerInfo = tokenManager.getCustomerInfo()
        when (userLogState) {
            UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                if (customerInfo != null) {
                    mainViewModel.setCustomerInfo(customerInfo)
                }
            }

            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                if (customerInfo != null) {
                    mainViewModel.setCustomerInfo(customerInfo)
                }
            }

            UserLogState.LOGGED_IN_AS_USER -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_IN_AS_USER)
            }

            UserLogState.LOGGED_OUT -> {
                mainViewModel.setUserLogState(UserLogState.LOGGED_OUT)
            }
        }
    }

    // Check User is logged in or not
    private fun checkUserLoggedIn() {
        val tokenManager = TokenManager(this)
        if (tokenManager.isLoggedIn()) {
            // Decode JWT token to JWTPayloadModel
            val jwtPayloadModel =
                JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken().toString())
            // Save tokens to shared preferences
            jwtPayloadModel?.let { mainViewModel.updateUser(it) }
        }
    }


    /**
     * Handle Bottom Navigation Logic
     */
    private fun setUpBottomNav() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(HomeFragment())
        listFragment.add(TopResortFragment())
        listFragment.add(PostingFragment())
        listFragment.add(ExchangeFragment())
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
    }


}