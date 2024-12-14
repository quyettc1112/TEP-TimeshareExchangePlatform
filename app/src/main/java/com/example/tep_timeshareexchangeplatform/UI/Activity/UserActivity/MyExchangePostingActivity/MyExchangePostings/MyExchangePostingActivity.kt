package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.Adapter.MyExchangePostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangePostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyExchangePostingActivity : BaseActivity() {
    private lateinit var binding: ActivityMyExchangePostingBinding
    private lateinit var exchangeAdapter: MyExchangePostingAdapter
    private val viewModel: MyExchangePostingViewModel by viewModels()
    private lateinit var tokenManager: TokenManager
    private var itemPosition = 0

    companion object {
        const val POSTING_PAGE_SIZE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyExchangePostingBinding.inflate(layoutInflater)
        exchangeAdapter = MyExchangePostingAdapter(this)
        setContentView(binding.root)
        tokenManager = TokenManager(this)
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
        initAdapter()
        bindDataMyPostingList()
        eventClickToolbar()
    }

    private fun eventClickToolbar() {
        binding.customToolbar.onStartIconClick = {
            finish()
        }
    }

    private fun initAdapter() {
        exchangeAdapter.onItemClick = {
            val intent = Intent(this, MyExchangeDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_MY_POSTING_ID, it.exchangePostingId)
            startActivity(intent)
        }

        exchangeAdapter.onHidePostingClick = {
            showConfirmDialog(
                "Ẩn bài đăng",
                "Bạn có chắc chắn muốn ẩn bài đăng này không?",
                "Đồng ý",
                "Hủy",
                "",
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {
                        // Do nothing
                    }

                    override fun positiveAction() {
                        viewModel.deActiveExchangePosting(
                            tokenManager.getAccessToken().toString(),
                            it.exchangePostingId
                        )
                    }
                }
            )
        }

        exchangeAdapter.onHidePostingPositionClick = {
            itemPosition = it
        }
    }

    private fun observeData() {
        viewModel.myExchangePostingList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    if (it.data?.totalPages == 0) {
                        showInfoDialog(
                            this,
                            "Bạn chưa có bài đăng Trao Đổi nào",
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    finish()
                                }
                            }
                        )
                    } else {
                        binding.animLoadingMore.visibility = View.GONE
                        viewModel.loadMorePostingList(it.data?.content ?: listOf())
                        exchangeAdapter.submitList(viewModel.getCurrentPostingList())
                    }
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                    showErrorToast("Lỗi", "Không thể tải dữ liệu")
                }
            }
        }
        viewModel.currentPage.observe(this) {
            viewModel.getMyExchangePostingList(
                TokenManager(this).getAccessToken().toString(),
                it,
                POSTING_PAGE_SIZE
            )
        }
        viewModel.deactivateExchangePosting.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessToast("Thành Công", "Ẩn bài đăng thành công")
                    exchangeAdapter.updateItemStatus(itemPosition, MyPostingStatus.CLOSED.name)
                    val id = exchangeAdapter.getItemIdFromPosition(itemPosition) ?: 0
                    viewModel.updatePostingItem(id, MyPostingStatus.CLOSED.name)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Lỗi", "Không thể ẩn bài đăng")
                }
            }
        }
    }


    private fun bindDataMyPostingList() {
        binding.rvMyPosting.apply {
            adapter = exchangeAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(
                    this@MyExchangePostingActivity,
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
                val totalPages = viewModel.myExchangePostingList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPage()
                }
            }
        })
    }
}