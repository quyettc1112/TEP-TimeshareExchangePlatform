package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MemberShipActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MemberShipActivity.Adapter.MemberShipAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.PaymentScreen.PaymentPackageActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMemberShipBinding

class MemberShipActivity : BaseActivity() {
    private lateinit var binding: ActivityMemberShipBinding
    private var memberShipAdapter = MemberShipAdapter()
    private val memberShipViewModel : MemberShipViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMemberShipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        initViewPagerView()

        binding.customToolbar4.onStartIconClick = {
            onBackPressed()
            finish()
        }

        binding.imgNext.setOnClickListener {
           startActivity(Intent(this, RentalPostingActivity::class.java))
        }
        clickRequestPaymentButton()

    }

    private fun initAdapter() {
        memberShipAdapter.submitList(Constant.listMemberShip)
    }

    private fun initViewPagerView() {
        binding.vpMemberShip.adapter = memberShipAdapter
        // Set Indicator
        binding.indicator.apply {
            setViewPager(binding.vpMemberShip)
            tintIndicator(ContextCompat.getColor(context, R.color.blue_full))
        }
        // Lắng nghe sự kiện thay đổi trang
        binding.vpMemberShip.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        memberShipViewModel.updateCurrentPackage(PackageEnum.MEMBERSHIP_MONTHLY.packageModel)
                        Log.d("CheckCurrentPackage", "Updated to MONTHLY")
                    }
                    1 -> {
                        memberShipViewModel.updateCurrentPackage(PackageEnum.MEMBERSHIP_YEARLY.packageModel)
                        Log.d("CheckCurrentPackage", "Updated to YEARLY")
                    }
                }
            }
        })
    }

    private fun clickRequestPaymentButton() {
        binding.ctrRequestButton.setOnClickListener {
            val dialogFragment = MemberInfoDialog.newInstance()
            dialogFragment.show(supportFragmentManager, dialogFragment.tag)
            dialogFragment.setOnClickRequestButton(object : MemberInfoDialog.OnClickRequestButton {
                override fun onClickRequestButton() {
                    val intent = Intent(this@MemberShipActivity, PaymentPackageActivity::class.java)
                    intent.putExtra(Constant.DEFAULT_MEMBERSHIP_PACKAGE_SELECTION , memberShipViewModel.currentPackage.value!!.id)
                    startActivity(intent)
                }
            })

        }
    }


}