package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity

import android.content.Intent
import android.os.Bundle
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
        innitAdapter()
        bindDataMyPostingList()

    }
    private fun innitAdapter() {
        myPostingAdapter.submitList(Constant.listMyPosting)
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
            layoutManager = LinearLayoutManager(this@MyPostingActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}