package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var exchangeRequestOnPostAdapter: ExchangeRequestOnPostAdapter
    private val viewModel: ExchangeRequestOnPostViewModel by viewModels()
    private var postingId: Int = 0
    private lateinit var detailActivityLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val POSTING_PAGE_SIZE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExchangeRequestOnPostBinding.inflate(layoutInflater)
        exchangeRequestOnPostAdapter = ExchangeRequestOnPostAdapter(this)

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
            showWarningToast("Bạn chưa đăng nhập",getString(R.string.msg_need_login) )
        }
        initAdapter()
        bindDataRequestOnPostList()

        // Đăng ký ActivityResultLauncher
        detailActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                // Load lại danh sách
                viewModel.clearCurrentRequestOnPostList()
                exchangeRequestOnPostAdapter.apply {
                    submitList(listOf())
                    notifyDataSetChanged()
                }
                viewModel.currentPage.value = 0
            }
        }
    }

    private fun initAdapter() {
        exchangeRequestOnPostAdapter.onItemClick = {
            val intent = Intent(this, MyExchangeRequestDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, it.id)
            detailActivityLauncher.launch(intent)
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
                    exchangeRequestOnPostAdapter.submitList(viewModel.getCurrentRequestOnPostList())
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                    showErrorToast("Lỗi tải dữ liệu", "Không thể tải dữ liệu")

                }
            }
        }
        postingId = intent.getIntExtra(Constant.DEFAULT_EXCHANGE_REQUEST_ON_POST, 0)
        viewModel.currentPage.observe(this) { currentPage ->
            viewModel.getExchangeRequestOnPostList(
                TokenManager(this).getAccessToken().toString(),
                postingId,
                currentPage,
                ExchangeRequestOnPostActivity.POSTING_PAGE_SIZE,
            )
        }
    }


    private fun bindDataRequestOnPostList() {
        binding.requestOnPost.apply {
            adapter = exchangeRequestOnPostAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(
                    this@ExchangeRequestOnPostActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
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

    override fun onResume() {
        super.onResume()
        viewModel.clearCurrentRequestOnPostList()
        exchangeRequestOnPostAdapter.apply {
            submitList(viewModel.getCurrentRequestOnPostList())
            notifyDataSetChanged()
        }
        viewModel.currentPage.value = 0
    }
}