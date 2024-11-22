package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity.BlogDetailActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogDetailResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBlogDetailBinding

class BlogDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityBlogDetailBinding
    private val viewModel: BlogDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBlogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeBlogDetail()
        binding.customToolbar.onStartIconClick = {
            finish()
        }
    }
    private fun observeBlogDetail() {
        viewModel.blogDetail.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        this,
                        "Lỗi",
                        it.message.toString(),
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
    private fun bindData(blogDetail: BlogDetailResponse) {
        // Blog Info
        binding.apply {
            tvBlogDetailTitle.text =
                blogDetail.title
            tvBlogDetailContent.text =
                blogDetail.content
            tvBlogDetailDate.text =
                blogDetail.createdAt
            Glide.with(this@BlogDetailActivity)
                .load(blogDetail.image)
                .into(imgBlogDetail)
        }
    }

}