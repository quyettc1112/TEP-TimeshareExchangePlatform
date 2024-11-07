package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
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
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.Adapter.MyPostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingDetailActivity.MyPostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.PricingSupportActivity.PricingSupportActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyPostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostingActivity : BaseActivity() {
    private lateinit var binding: ActivityMyPostingBinding

    private val viewModel: MyPostingViewModel by viewModels()

    private lateinit var myPostingAdapter: MyPostingAdapter
    private lateinit var acceptPriceLauncher: ActivityResultLauncher<Intent>


    companion object {
        const val POSTING_PAGE_SIZE = 10
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyPostingBinding.inflate(layoutInflater)
        myPostingAdapter = MyPostingAdapter(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val token = TokenManager(this)
        if (token.isLoggedIn() && token.getAccessToken() != null) {
            observeMyPostingList()
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
        initActivityLauncher()
        innitAdapter()
        bindDataMyPostingList()

        binding.customToolbar.onStartIconClick = {
            onBackPressed()
        }

    }

    private fun observeMyPostingList() {
        viewModel.myPostingList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    viewModel.loadMorePostingList(it.data?.content ?: listOf())
                    myPostingAdapter.submitList(viewModel.getCurrentPostingList())
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                    it.message?.let { it1 ->
                        MotionToast.Companion.createColorToast(
                            this,
                            "Lỗi",
                            it1,
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null
                        )
                    }
                }
            }
        }

        viewModel.currentPostingPage.observe(this) {
            Toast.makeText(this, "Page: $it", Toast.LENGTH_SHORT).show()
            viewModel.getMyPostingList(
                TokenManager(this).getAccessToken().toString(),
                it,
                POSTING_PAGE_SIZE
            )
        }

    }

    private fun innitAdapter() {
        myPostingAdapter.submitList(listOf())
        myPostingAdapter.onItemClick = {
            val intent = Intent(this, MyPostingDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_MY_POSTING_ID, it.rentalPostingId)
            startActivity(intent)
        }

        myPostingAdapter.onItemPricingClick = {
            val intent = Intent(this, PricingSupportActivity::class.java)
            // Id
            intent.putExtra(Constant.DEFAULT_MY_POSTING_ID, it.rentalPostingId)

            // Package Name
            intent.putExtra(Constant.DEFAULT_PACKAGE_SELECTION, it.rentalPackageName)

            // Night
            intent.putExtra(Constant.DEFAULT_MY_POSTING_NIGHT, it.nights)

            // Name
            intent.putExtra(Constant.DEFAULT_MY_POSTING_RESORT_NAME, it.resortName)

            // Room Name
            intent.putExtra(Constant.DEFAULT_MY_POSTING_ROOM_NAME, it.roomName)

            // Check In Date
            intent.putExtra(
                Constant.DEFAULT_MY_POSTING_CHECK_IN_DATE,
                Constant.formatDateByLocale(it.checkinDate, this)
            )

            // Check Out Date
            intent.putExtra(
                Constant.DEFAULT_MY_POSTING_CHECK_OUT_DATE,
                Constant.formatDateByLocale(it.checkoutDate, this)
            )

            val packageEnum = PackageEnum.getPackageByName(it.rentalPackageName)
            when (packageEnum) {
                PackageEnum.PREMIUM_SERVICE.packageModel -> {
                    intent.putExtra(Constant.staffRefinementPrice, it.staffRefinementPrice)
                }

                PackageEnum.DELEGATED_SERVICE.packageModel -> {
                    intent.putExtra(Constant.priceValuation, it.priceValuation)
                }
            }
            acceptPriceLauncher.launch(intent)
        }
    }

    private fun bindDataMyPostingList() {
        binding.rvMyPosting.apply {
            adapter = myPostingAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@MyPostingActivity, LinearLayoutManager.VERTICAL, false)
        }

        // Scroll Listener
        binding.rvMyPosting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.myPostingList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPostingPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPostingsPage()
                    Toast.makeText(this@MyPostingActivity, "Load More", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initActivityLauncher() {
        acceptPriceLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.clearCurrentPostingList()
                    myPostingAdapter.submitList(listOf())
                    viewModel.currentPostingPage.value = 0
                }
            }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}