package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter.MyExchangeRequestAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.Adapter.MyExchangePostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity.Companion
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestDetailActivity.MyExchangeRequestDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity.MyPostingDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangePostingBinding
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangeRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyExchangeRequestActivity : BaseActivity() {
    private lateinit var binding: ActivityMyExchangeRequestBinding
    private lateinit var exchangeAdapter: MyExchangeRequestAdapter
    private lateinit var detailActivityLauncher: ActivityResultLauncher<Intent>
    private val viewModel: MyExchangeRequestViewModel by viewModels()

    companion object {
        const val POSTING_PAGE_SIZE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyExchangeRequestBinding.inflate(layoutInflater)
        exchangeAdapter = MyExchangeRequestAdapter(this)
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
            showErrorToast("Bạn chưa đăng nhập", "Vui lòng đăng nhập để xem thông tin")
        }
        binding.customToolbar.onStartIconClick = {
            finish()
        }
        initAdapter()
        bindDataMyPostingList()

        // Đăng ký ActivityResultLauncher
        detailActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Load lại danh sách nếu cần
                viewModel.clearCurrentRequestList()
                exchangeAdapter.apply {
                    submitList(viewModel.getCurrentRequestList())
                    notifyDataSetChanged()
                }
                viewModel.currentPage.value = 0
            }
        }
    }

    private fun initAdapter() {
        exchangeAdapter.onItemClick = {
            val intent = Intent(this, MyExchangeRequestDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, it.id)
            detailActivityLauncher.launch(intent)
        }
    }

    private fun observeData() {
        viewModel.myExchangeRequestList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    if (it.data?.totalPages == 0) {
                        showInfoDialog(
                            this,
                            "Bạn chưa có bài yêu cầu trao đổi nào",
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    finish()
                                }
                            }
                        )
                    } else {
                        binding.animLoadingMore.visibility = View.GONE
                        viewModel.loadMoreRequestList(it.data?.content ?: listOf())
                        exchangeAdapter.submitList(viewModel.getCurrentRequestList())
                    }
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                    showErrorToast("Lỗi", it.message ?: "Có lỗi xảy ra")
                }
            }
        }

        viewModel.currentPage.observe(this) {
            viewModel.getMyExchangeRequestList(
                TokenManager(this).getAccessToken().toString(),
                it,
                MyExchangeRequestActivity.POSTING_PAGE_SIZE
            )
        }
    }

    private fun bindDataMyPostingList() {
        binding.rvMyPosting.apply {
            adapter = exchangeAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(
                    this@MyExchangeRequestActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }

        // Scroll Listener
        binding.rvMyPosting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.myExchangeRequestList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPage()
                }
            }
        })
    }
}