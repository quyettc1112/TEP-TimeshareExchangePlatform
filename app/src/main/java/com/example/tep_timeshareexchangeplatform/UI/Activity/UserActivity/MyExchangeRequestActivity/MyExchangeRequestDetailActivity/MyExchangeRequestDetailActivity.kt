package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestDetailActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity.ExchangeRequestOnPostActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyExchangeRequestStatus
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangeRequestDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyExchangeRequestDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyExchangeRequestDetailBinding
    private lateinit var imagePostingAdapter: ImagePostingAdapter
    private val viewModel: MyExchangeRequestDetailViewModel by viewModels()
    private lateinit var tokenManager: TokenManager
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

        binding.customToolbar5.onStartIconClick = {
            finish()
        }
        evenClickApproveExchangeRequest()
        eventClickRejectExchangeRequest()


    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
        tokenManager = TokenManager(this)
        if (tokenManager.isLoggedIn() && tokenManager.getAccessToken() != null) {
            viewModel.getCustomerExchangeDetail(tokenManager.getAccessToken().toString(), intent)
            observeMyExchangeRequestDetail()
        } else {
            showWarningToast("Bạn chưa đăng nhập", "Vui lòng đăng nhập để xem thông tin")
        }
    }

    private fun observeMyExchangeRequestDetail() {
        // Get Detail
        viewModel.myExchangeRequestDetail.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                    Log.d("MyExchangeRequestDetail", it.data.ownerId.toString())
                    Log.d("MyExchangeRequestDetail", tokenManager.getProfileInfo()?.id.toString())

                    /* when(MyExchangeRequestStatus.fromApiStatus(it.data.status)!!){
                         MyExchangeRequestStatus.PENDING_APPROVAL -> {
                             binding.btnAccept.visibility = View.GONE
                         }
                         MyExchangeRequestStatus.PENDING_OWNER -> {
                             binding.btnAccept.visibility = View.GONE
                         }

                         MyExchangeRequestStatus.PENDING_CUSTOMER -> {
                             binding.btnAccept.visibility = View.VISIBLE
                         }
                         MyExchangeRequestStatus.COMPLETED -> {
                             binding.btnAccept.visibility = View.GONE
                         }
                         MyExchangeRequestStatus.REJECTED -> {
                             binding.btnAccept.visibility = View.GONE
                         }
                     }
                     if (tokenManager.getProfileInfo()?.id == it.data.ownerId) {
                         binding.btnAccept.visibility = View.GONE

                     } else {
                         binding.btnAccept.visibility = View.VISIBLE
                     }*/


                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi lấy thông tin yêu cầu trao đổi")
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Approve Exchange Request
        viewModel.approveExchangeRequest.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this,
                        "Duyệt yêu cầu trao đổi thành công",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val intent = Intent(
                                    this@MyExchangeRequestDetailActivity,
                                    MyExchangeDetailActivity::class.java
                                )
                                intent.putExtra(
                                    Constant.DEFAULT_MY_POSTING_ID,
                                    it.data?.exchangePosting?.id
                                )
                                startActivity(intent)
                            }

                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi duyệt yêu cầu trao đổi")
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Reject Exchange Request
        viewModel.rejectExchangeRequest.observe(this) { data ->
            when (data.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this,
                        "Từ chối yêu cầu trao đổi thành công",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val intent = Intent(
                                    this@MyExchangeRequestDetailActivity,
                                    ExchangeRequestOnPostActivity::class.java
                                )
                                intent.putExtra(Constant.DEFAULT_EXCHANGE_REQUEST_ON_POST, data.data?.exchangePosting?.id)
                                startActivity(Intent(this@MyExchangeRequestDetailActivity, ExchangeRequestOnPostActivity::class.java))
                            }

                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi từ chối yêu cầu trao đổi")
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

        // Resort Info
        binding.apply {
            tvResortName.text =
                myExchangeRequestDetail.exchangePosting.roomInfoResortResortName
            tvRoomCode.text = myExchangeRequestDetail.roomInfo.location.displayName
            Glide.with(this@MyExchangeRequestDetailActivity)
                .load(myExchangeRequestDetail.roomInfo.unitType.photos)
                .into(imImageTimeshare)
            tvNights.text = myExchangeRequestDetail.exchangePosting.nights.toString() + " đêm"
            if (myExchangeRequestDetail.exchangePosting.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Exchanger
        binding.apply {
            tvOwnerFullName.text = myExchangeRequestDetail.ownerFullName
            Glide.with(this@MyExchangeRequestDetailActivity)
                .load(myExchangeRequestDetail.ownerAvatar)
                .placeholder(R.drawable.ic_image_tmp_holder)
                .error(R.drawable.ic_image_tmp_holder)
                .into(ivOwnerAvatar)
        }

        // Check in Date, Check out Date
        binding.apply {
            tvCheckInDate.text = Constant.formatDateByLocale(
                myExchangeRequestDetail.startDate,
                this@MyExchangeRequestDetailActivity
            )
            tvCheckOutDate.text =
                Constant.formatDateByLocale(
                    myExchangeRequestDetail.endDate,
                    this@MyExchangeRequestDetailActivity
                )
        }

        // Price Valuation
        binding.apply {
            if (myExchangeRequestDetail.priceValuation != null) {
                etPriceInput.setText(Constant.formatPriceLong(myExchangeRequestDetail.priceValuation))
            } else {
                etPriceInput.setText("Không Có Đề Xuất Giá Chênh Lệch")
            }
        }

        // Note
        binding.apply {
            if (myExchangeRequestDetail.note != null) {
                etNote.setText(myExchangeRequestDetail.note.toString())
            } else {
                etNote.setText("Người Gửi Không Để Lại Lời Nhắn")
            }
        }

        // Status
        bindDataStatus(myExchangeRequestDetail)


    }

    private fun bindDataStatus(item: MyExchangeRequestDetailResponse) {
        // Show Status
        when (MyExchangeRequestStatus.fromApiStatus(item.status)) {
            MyExchangeRequestStatus.PENDING_OWNER -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.blue_full
                )
            }

            MyExchangeRequestStatus.PENDING_APPROVAL -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.PENDING_RENTER_PRICING -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.PENDING_RENTER_PAYMENT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.PENDING_OWNER_PAYMENT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.COMPLETED -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.green_verify
                )
            }


            MyExchangeRequestStatus.REJECT_APPROVAL -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            MyExchangeRequestStatus.RENTER_REJECT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            MyExchangeRequestStatus.OWNER_REJECT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            else -> {
                // Default or unknown status case
                applyStatusStyle(
                    this,
                    R.color.status_unknown_bg,
                    R.color.status_unknown_text
                )
            }
        }

        binding.tvStatus.text =
            MyExchangeRequestStatus.fromApiStatus(item.status)?.getDescription(this)
                ?: ""
    }

    private fun evenClickApproveExchangeRequest() {

    }

    private fun eventClickRejectExchangeRequest() {
        binding.btnReject.setOnClickListener {
            showConfirmDialog(
                title = "Từ Chối",
                message = "Bạn có chắc chắn muốn từ chối yêu cầu trao đổi này không?",
                positiveButtonTitle = "Từ Chối",
                negativeButtonTitle = "Hủy",
                "",
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {}
                    override fun positiveAction() {
                        callRejectExchangeRequest()
                    }
                },
            )
        }
    }

    private fun callApproveExchangeRequest() {
        val requestId = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
        viewModel.approveExchangeRequest(tokenManager.getAccessToken().toString(), requestId)
    }

    private fun callRejectExchangeRequest() {
        val requestId = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
        viewModel.rejectExchangeRequest(tokenManager.getAccessToken().toString(), requestId)
    }

    private fun bindDataUnitType(data: MyExchangeRequestDetailResponse) {
        // Set Unit Type Of Posting
        binding.includeUnitType.apply {
            btnViewDetail.visibility = View.GONE
            tvRoomCode.text = data.roomInfo.roomInfoCode
            tvRoomType.text = data.roomInfo.unitType.title
            llRoomName.visibility = View.GONE
            llUnityTypeBaseAmeniites.visibility = View.GONE
            root.visibility = View.VISIBLE

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

    private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
        binding.apply {
            llStatusContainer.visibility = View.VISIBLE
            llStatusContainer.backgroundTintList =
                ResourcesCompat.getColorStateList(context.resources, backgroundColorRes, null)
            tvStatus.setTextColor(context.getColor(textColorRes))
            cardStatus.setStrokeColor(context.getColor(textColorRes))
        }
    }
}