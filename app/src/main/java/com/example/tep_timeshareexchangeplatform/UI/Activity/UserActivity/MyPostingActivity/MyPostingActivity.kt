package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.Adapter.MyPostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingDetailActivity.MyPostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.PricingSupportActivity.PricingSupportActivity
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyPostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostingActivity : BaseActivity() {
    private lateinit var binding: ActivityMyPostingBinding

    private val viewModel: MyPostingViewModel by viewModels()

    private var myPostingAdapter = MyPostingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val token = TokenManager(this)
        if (token.isLoggedIn() && token.getAccessToken() != null) {
            viewModel.getMyPostingList(token.getAccessToken().toString())
        } else  {
            MotionToast.Companion.createColorToast(
                this,
                "Bạn chưa đăng nhập",
                "Vui lòng đăng nhập để xem thông tin",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
        }

        innitAdapter()
        bindDataMyPostingList()
        observeMyPostingList()

    }

    private fun observeMyPostingList() {
        viewModel.myPosting.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    it.data?.let { list ->
                        myPostingAdapter.submitList(list)
                        Log.d("MyPostingActivityDasta", "observeMyPostingList: $list")
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        this,
                        "Lỗi",
                        "Không thể lấy thông tin bài đăng",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }


    }


    private fun innitAdapter() {
        myPostingAdapter.submitList(listOf())
        myPostingAdapter.onItemClick = {
            startActivity(Intent(this, MyPostingDetailActivity::class.java))
        }

        myPostingAdapter.onItemPricingClick = {
            startActivity(Intent(this, PricingSupportActivity::class.java))
        }
    }

    private fun bindDataMyPostingList() {
        binding.rvMyPosting.apply {
            adapter = myPostingAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@MyPostingActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}