package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.FeedbackListActivity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityFeedbackListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackListActivity : BaseActivity() {
    private lateinit var binding: ActivityFeedbackListBinding
    private val viewModel: FeedbackListViewModel by viewModels()
    private val feedbackListAdapter = FeedbackListAdapter()
    private var resortId: Int = 0

    companion object {
        const val FEEDBACK_PAGE_SIZE = 15
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFeedbackListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        bindDataListFeedback()
        eventClickToolbar()
        getIntentValue()
    }

    private fun getIntentValue(){
        val resortIdIntent = intent.getIntExtra(Constant.DEFAULT_RESORT_ID, 0)
        resortId = resortIdIntent
        if (resortId == 0) {
            finish()
        }
        val avgRating = intent.getFloatExtra(Constant.AVG_RATING, 0f)
        val totalRating = intent.getIntExtra(Constant.TOTAL_RATING, 0)
        binding.tvAvgRating.text = avgRating.toString()
        binding.tvReviewCount.text = "$totalRating đánh giá"

        observeData()
    }

    private fun observeData() {
        viewModel.feedBackResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    binding.lottieLoading.visibility  =View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.lottieLoading.visibility = View.GONE
                    viewModel.loadMoreFeedbackList(it.data?.content ?: listOf())
                    feedbackListAdapter.submitList(viewModel.getCurrentFeedbackList())

                }

                Status.ERROR -> {
                    binding.lottieLoading.visibility = View.GONE
                    MotionToast.createColorToast(
                        this,
                        "Error",
                        it.message ?: "Error",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        })
        viewModel.currentPage.observe(this, {
            viewModel.callGetListFeedbackByResortId(resortId, it, FEEDBACK_PAGE_SIZE)
        })
    }


    private fun eventClickToolbar() {
        binding.toolbar.onStartIconClick = {
            onBackPressed()
        }
    }

    private fun initAdapter() {
        feedbackListAdapter.submitList(listOf())

    }

    private fun bindDataListFeedback() {
        binding.rvReview.apply {
            adapter = feedbackListAdapter
            layoutManager =
                LinearLayoutManager(this@FeedbackListActivity, RecyclerView.VERTICAL, false)
        }


        binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val view = binding.nestedScrollView.getChildAt(binding.nestedScrollView.childCount - 1)
            val diff = (view.bottom - (binding.nestedScrollView.height + scrollY))

            if (diff == 0) { // Kiểm tra cuộn đến cuối cùng
                val layoutManager = binding.rvReview.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.feedBackResponse.value?.data?.totalPages ?: 0
                if (lastVisibleItem == totalItemCount - 1 &&
                    viewModel.currentPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPage()
                }
            }
        }
    }


}