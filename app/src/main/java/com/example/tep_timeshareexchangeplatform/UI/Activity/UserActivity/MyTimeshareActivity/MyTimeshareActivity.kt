package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity.MyTimeshareDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.MyTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.ViewModel.MyTimeshareViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyTimeshareBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTimeshareActivity : BaseActivity() {

    private lateinit var binding: ActivityMyTimeshareBinding
    private var myTimeshareAdapter = MyTimeshareAdapter()
    private lateinit var tokenManager: TokenManager
    private val myTimeshareViewModel: MyTimeshareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTimeshareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)

        if (!tokenManager.isLoggedIn()) {
            MotionToast.Companion.createToast(
                this,
                "Error",
                "Bạn chưa đăng nhập",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
            finish()
        } else {
            myTimeshareViewModel.getMyTimeshareList(tokenManager.getAccessToken().toString(), 0, 10)
        }

        tokenManager = TokenManager(this)
        initAdapter()
        observeData()
        setEventItemClick()

    }

    private fun initAdapter() {
        myTimeshareAdapter.submitList(listOf())
        binding.recyclerView.let {
            it.adapter = myTimeshareAdapter
            it.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun observeData() {
        myTimeshareViewModel.myTimeshareList.observe(this) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    resources.data?.let {
                        if (it.content.isEmpty()) {
                            showInfoDialog(
                                this,
                                "Bạn chưa có Timeshare nào",
                                object : View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        finish()
                                    }
                                }
                            )
                        } else {
                            myTimeshareAdapter.submitList(it.content)
                        }

                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (resources.message!!.contains("404")) {
                        showInfoDialog(
                            this,
                            "Bạn chưa có Timeshare nào",
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    finish()
                                }
                            }
                        )
                    } else
                        MotionToast.Companion.createToast(
                            this,
                            "Error",
                            resources.message.toString(),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null
                        )

                }
            }
        }


    }

    private fun setEventItemClick() {
        // Item click
        myTimeshareAdapter.setItemOnclickListener {
            val intent = Intent(this, MyTimeshareDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, it.timeShareId)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
    }
}