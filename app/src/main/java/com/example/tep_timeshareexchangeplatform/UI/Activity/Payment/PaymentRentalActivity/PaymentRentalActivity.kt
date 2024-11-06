package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.HomeFragment.HomeFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.TopResortFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.Fragment.Step_1_PaymentRentalFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.Fragment.Step_2_PaymentRentalFragment
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRentalPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentRentalActivity : BaseActivity() {
    private lateinit var binding: ActivityRentalPaymentBinding
    private lateinit var FragmentAdapter: FragmentAdapter
    private val paymentRentalViewModel: PaymentRentalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRentalPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpPaymentFragment()
        getIntentData()

        paymentRentalViewModel.currentViewPager.observe(this) { page ->
            binding.vp2Main.currentItem = page
        }

    }

    private fun getIntentData() {
        val intent = intent
        if (intent != null) {
            val postingId = intent.getIntExtra(Constant.DEFAULT_POSTING_ID, 0)
            if (postingId != null) {
                paymentRentalViewModel.getPostingDetail(postingId)
            }
        }
    }



    private fun setUpPaymentFragment() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(Step_1_PaymentRentalFragment())
        listFragment.add(Step_2_PaymentRentalFragment())

        FragmentAdapter = FragmentAdapter(this, listFragment)

        binding.vp2Main.apply {
            adapter = FragmentAdapter
            isUserInputEnabled = false
            offscreenPageLimit = 2
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    paymentRentalViewModel.setCurrentViewPager(position)
                }
            })
        }

    }


}