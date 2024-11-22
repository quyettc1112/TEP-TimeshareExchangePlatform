package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity.BlogDetailActivity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBlogDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import java.net.URL

@AndroidEntryPoint
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
        initAdapter()
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
    private fun initAdapter() {
        val postingId = intent.getIntExtra(Constant.DEFAULT_BLOG_ID, 0)
        viewModel.getBlogDetail(postingId)
    }

    private fun bindData(blogDetail: BlogDetailResponse) {
        binding.apply {
            tvBlogDetailTitle.text = blogDetail.title
            tvBlogDetailContent.text = blogDetail.content.toHtmlSpannedWithImages(this@BlogDetailActivity)
            tvBlogDetailDate.text = blogDetail.createdAt
            Glide.with(this@BlogDetailActivity)
                .load(blogDetail.image)
                .into(imgBlogDetail)
        }
    }

    private fun String.toHtmlSpannedWithImages(context: Context): Spanned {
        val imageGetter = Html.ImageGetter { source ->
            try {
                // Use Glide to fetch the image (asynchronously) and return a placeholder drawable
                val drawable = Glide.with(context)
                    .asDrawable()
                    .load(source)
                    .submit()
                    .get()

                drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                drawable
            } catch (e: Exception) {
                // Return a placeholder if the image fails to load
                val placeholder = context.getDrawable(R.drawable.placeholder_image)
                placeholder?.setBounds(0, 0, placeholder.intrinsicWidth, placeholder.intrinsicHeight)
                placeholder
            }
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
        } else {
            Html.fromHtml(this, imageGetter, null)
        }
    }

}