package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity.Adapter.BlogListAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity.Companion
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBlogListBinding

class BlogListActivity : BaseActivity() {
    private lateinit var binding: ActivityBlogListBinding
    private lateinit var blogListAdapter: BlogListAdapter
    private val viewModel: BlogListViewModel by viewModels()

    companion object{
        const val POSTING_PAGE_SIZE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBlogListBinding.inflate(layoutInflater)
        blogListAdapter = BlogListAdapter(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeData()
        initAdapter()
        bindDataMyPostingList()
    }

    private fun initAdapter() {
        blogListAdapter.onItemClick = {
            val intent = Intent(this, MyExchangeDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_BLOG_ID, it.id)
            startActivity(intent)
        }
    }


    private fun observeData() {
        viewModel.blogList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    viewModel.loadMoreBlogList(it.data?.content ?: listOf())
                    blogListAdapter.submitList(viewModel.getCurrentBlogList())

                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                    MotionToast.Companion.createColorToast(
                        this,
                        "Lỗi",
                        it.message ?: "Có lỗi xảy ra",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        viewModel.currentPage.observe(this) {
            viewModel.getBlogList(
                it,
                BlogListActivity.POSTING_PAGE_SIZE,""
            )
        }
    }


    private fun bindDataMyPostingList() {
        binding.rvBlogList.apply {
            adapter = blogListAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@BlogListActivity, LinearLayoutManager.VERTICAL, false)
        }

        // Scroll Listener
        binding.rvBlogList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.blogList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPage()
                }
            }
        })
    }
}