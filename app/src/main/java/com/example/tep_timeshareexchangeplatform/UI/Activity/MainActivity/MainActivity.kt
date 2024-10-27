package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import android.content.res.Configuration
import android.os.Bundle
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
class MainActivity : BaseActivity(), OnBottomNavVisibilityListener{

    lateinit var binding: ActivityMainBinding
    private lateinit var FragmentAdapter: FragmentAdapter

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUserLogState()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        changeLangEvent()
        setUpBottomNav()

    }

    private fun setUpUserLogState() {
        val intent = intent
        val userLogState = intent.getSerializableExtra(Constant.USER_LOGIN_STATE) as UserLogState
        when (userLogState) {
            UserLogState.LOGGED_IN_AS_USER -> {
                Toast.makeText(this, "Logged in as user", Toast.LENGTH_SHORT).show()
            }
            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                Toast.makeText(this, "Logged in as customer", Toast.LENGTH_SHORT).show()
            }

            UserLogState.LOGGED_OUT -> {
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            }
        }
    }


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
    private fun setUpBottomNav(){
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
            onItemSelected = {idFragemnt ->
                binding.vp2Main.setCurrentItem(idFragemnt, true)
            }
        }
    }
    override fun hideBottomNav() {
        binding.cardView.animate().translationY(binding.cardView.height.toFloat()).duration = 30
    }
    override fun showBottomNav() {
        binding.cardView.animate().translationY(0f).duration = 30
    }


    // Check User is logged in or not
    private fun checkUserLoggedIn() {
        val tokenManager = TokenManager(this)
        if (tokenManager.isLoggedIn()) {
            // Decode JWT token to JWTPayloadModel
            val jwtPayloadModel = JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken().toString())
            // Save tokens to shared preferences
            jwtPayloadModel?.let { mainViewModel.updateUser(it) }
        }
    }



    override fun onResume() {
        super.onResume()
        checkUserLoggedIn()
    }


}