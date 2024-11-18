package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.FeedbackListActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityFeedbackListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackListActivity : BaseActivity() {
    private lateinit var binding: ActivityFeedbackListBinding
    private val viewModel: FeedbackListViewModel by viewModels()
    private val feedbackListAdapter = ReviewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feedback_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        eventClickToolbar()
    }
    private fun observeData() {

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
            layoutManager = LinearLayoutManager(this@FeedbackListActivity, RecyclerView.VERTICAL, false)
        }


        // Scroll Listener
        binding.rvReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                //val totalPages = viewModel..value?.data?.totalPages ?: 0
               /* if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPage()
                }*/
            }
        })
    }


}