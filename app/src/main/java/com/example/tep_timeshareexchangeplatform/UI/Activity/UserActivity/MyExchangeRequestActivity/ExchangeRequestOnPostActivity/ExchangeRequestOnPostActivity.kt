package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity

import android.content.Intent
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
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter.ExchangeRequestOnPostAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter.MyExchangeRequestAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestDetailActivity.MyExchangeRequestDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityExchangePostingBinding
import com.example.tep_timeshareexchangeplatform.databinding.ActivityExchangeRequestOnPostBinding
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangeRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeRequestOnPostActivity : BaseActivity() {
    private lateinit var binding: ActivityExchangeRequestOnPostBinding
    private lateinit var exchangeAdapter: ExchangeRequestOnPostAdapter
    private val viewModel: ExchangeRequestOnPostViewModel by viewModels()
    private var postingId: Int = 0
    companion object{
        const val POSTING_PAGE_SIZE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExchangeRequestOnPostBinding.inflate(layoutInflater)
        exchangeAdapter = ExchangeRequestOnPostAdapter(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val token = TokenManager(this)
        if (token.isLoggedIn() && token.getAccessToken() != null) {
            observeData()
        } else {
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
        initAdapter()
        bindDataMyPostingList()
    }

    private fun initAdapter() {
        exchangeAdapter.onItemClick = {
            val intent = Intent(this, MyExchangeRequestDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, it.id)
            startActivity(intent)
        }
    }


    private fun observeData() {
        viewModel.myExchangeRequestOnPostList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    viewModel.loadMoreRequestOnPostList(it.data?.content ?: listOf())
                    exchangeAdapter.submitList(viewModel.getCurrentRequestOnPostList())

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
        postingId = intent.getIntExtra(Constant.DEFAULT_EXCHANGE_REQUEST_ON_POST, 0)
        viewModel.currentPage.observe(this) { currentPage ->
            viewModel.getMyExchangeRequestOnPostList(
                TokenManager(this).getAccessToken().toString(),
                currentPage,
                MyExchangeRequestActivity.POSTING_PAGE_SIZE,
                postingId
            )
        }
    }


    private fun bindDataMyPostingList() {
        binding.requestOnPost.apply {
            adapter = exchangeAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@ExchangeRequestOnPostActivity, LinearLayoutManager.VERTICAL, false)
        }

        // Scroll Listener
        binding.requestOnPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.myExchangeRequestOnPostList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPage()
                }
            }
        })
    }

    private fun getDetailValue(){
        var postingId: Int = 0
        postingId = intent.getIntExtra(Constant.DEFAULT_EXCHANGE_REQUEST_ON_POST, 0)

    }
}