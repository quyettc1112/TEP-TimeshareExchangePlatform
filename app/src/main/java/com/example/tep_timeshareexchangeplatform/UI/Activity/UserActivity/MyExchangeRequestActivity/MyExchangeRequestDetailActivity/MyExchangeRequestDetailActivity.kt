package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestDetailActivity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.UnitTypeDetailBottomSheet.UnitTypeDetailBottomSheet
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangeRequestDetailBinding

class MyExchangeRequestDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyExchangeRequestDetailBinding
    private lateinit var imagePostingAdapter: ImagePostingAdapter
    private val viewModel: MyExchangeRequestDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyExchangeRequestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getIntentValue()
        initAdapter()

        binding.customToolbar.onStartIconClick = {
            finish()
        }
        binding.shimmerViewContainer.startShimmer()
    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID_1, 0)
        val token = TokenManager(this)
        if (token.isLoggedIn() && token.getAccessToken() != null) {
            viewModel.getCustomerExchangeDetail(token.getAccessToken().toString(), intent)
            observeMyExchangeRequestDetail()
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
    }
    private fun observeMyExchangeRequestDetail() {
        viewModel.myExchangeRequestDetail.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                    binding.shimmerViewContainer.hideShimmer()
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
    private fun bindData(myExchangeRequestDetail: MyExchangeRequestDetailResponse) {


        // Unit Type
        bindDataUnitType(myExchangeRequestDetail)

        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${myExchangeRequestDetail.exchangePosting.roomInfoResortResortName}")
            setTitleDetail("Mã phòng: ${myExchangeRequestDetail.exchangePosting.roomInfoRoomInfoCode}")
        }

        // Resort Info
        binding.apply {
            tvResortName.text =
                myExchangeRequestDetail.exchangePosting.roomInfoResortResortName+ " | " + myExchangeRequestDetail.roomInfo.unitType.title
            Glide.with(this@MyExchangeRequestDetailActivity)
                .load(myExchangeRequestDetail.roomInfo.unitType.photos)
                .into(imageView)

            if (myExchangeRequestDetail.exchangePosting.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Check in Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                myExchangeRequestDetail.startDate,
                this@MyExchangeRequestDetailActivity
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(myExchangeRequestDetail.startDate, this@MyExchangeRequestDetailActivity)


            tvCheckoutDate.text = Constant.getFormattedDate(
                myExchangeRequestDetail.endDate,
                this@MyExchangeRequestDetailActivity
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(myExchangeRequestDetail.endDate, this@MyExchangeRequestDetailActivity)
        }
    }

    private fun bindDataUnitType(data : MyExchangeRequestDetailResponse) {
        // Set Unit Type Of Posting
        binding.includeUnitType.apply {
            tvRoomType.text ="Loại Phòng: " + data.roomInfo.unitType.title

            // Bath
            tvNumBath.text = data.roomInfo.unitType.bathrooms.toString()
            tvBed.text = data.roomInfo.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to data.roomInfo.unitType.bedsFull,
                "bedsKing" to data.roomInfo.unitType.bedsKing,
                "bedsSofa" to data.roomInfo.unitType.bedsSofa,
                "bedsMurphy" to data.roomInfo.unitType.bedsMurphy,
                "bedsQueen" to data.roomInfo.unitType.bedsQueen,
                "bedsTwin" to data.roomInfo.unitType.bedsTwin
            )
            tvNumBed.text = data.roomInfo.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = data.roomInfo.unitType.kitchen
            tvNumKitchen.text = 1.toString()

            // Max Guest
            tvNumPerson.text = data.roomInfo.unitType.sleeps.toString()
            tvPerson.text =
                "${data.roomInfo.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later
            // Unit Type Detail
            btnViewDetail.setOnClickListener {
                UnitTypeDetailBottomSheet(this@MyExchangeRequestDetailActivity, data.roomInfo.unitType).show()
            }
        }
    }
    private fun initAdapter() {
        imagePostingAdapter = ImagePostingAdapter()
    }

    fun displayBedsInfo(unitTypeMap: Map<String, Any>): String {
        val bedTypes = listOf(
            "bedsFull" to "Full",
            "bedsKing" to "King",
            "bedsSofa" to "Sofa",
            "bedsMurphy" to "Murphy",
            "bedsQueen" to "Queen",
            "bedsTwin" to "Twin"
        )

        val bedsList = bedTypes.mapNotNull { (key, label) ->
            val count = unitTypeMap[key] as? Int ?: 0 // Ép kiểu thành Int
            if (count > 0) "$count giường $label" else null
        }.joinToString(", ")

        return if (bedsList.isNotEmpty()) bedsList else "Không có giường"
    }
}