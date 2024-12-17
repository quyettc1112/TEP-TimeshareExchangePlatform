package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.Fragment.EmailFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.Fragment.NewPasswordFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.Fragment.TemporaryCodeFragment
import com.example.tep_timeshareexchangeplatform.databinding.ActivityForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var FragmentAdapter: FragmentAdapter
    private val viewModel : ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpScreenViewPager()

        viewModel.viewPagerPosition.observe(this) { position ->
            binding.vp2Main.setCurrentItem(position, true) // Cập nhật vị trí
        }


    }

    private fun setUpScreenViewPager() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(EmailFragment())
        listFragment.add(NewPasswordFragment())
        listFragment.add(TemporaryCodeFragment())


        FragmentAdapter = FragmentAdapter(this, listFragment)
        binding.vp2Main.apply {
            adapter = FragmentAdapter
            isUserInputEnabled = false
            offscreenPageLimit = 2
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setViewPagerPosition(position)
                }
            })
        }
    }
}