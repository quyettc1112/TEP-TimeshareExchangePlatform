package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentStep1PaymentRentalBinding

class Step_1_PaymentRentalFragment : BaseFragment(R.layout.fragment_step_1__payment_rental) {
    private val viewModel: PaymentRentalViewModel by activityViewModels()
    private lateinit var binding: FragmentStep1PaymentRentalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStep1PaymentRentalBinding.inflate(inflater, container, false)
        observeData()
        requestButtonClick()
        return binding.root
    }

    private fun observeData() {
        viewModel.postingDetail.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as PaymentRentalActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    Log.d("CheckPostingDetailData", "observePostingDetail: ${it.data}")
                    bindDataPostingDetail(it.data!!)
                }

                Status.ERROR -> {
                    (activity as PaymentRentalActivity).apply {
                        hideLoadingWaiting()
                        showErrorToast("Lỗi", "Không thể tải dữ liệu")
                    }

                }
            }
        }
    }

    private fun bindDataPostingDetail(postingDetail: PublicPostingDetailResponse) {
        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${postingDetail.resortName}")
            setTitleDetail("${postingDetail.checkinDate} đến ${postingDetail.checkoutDate}")
        }

        binding.customToolbar.onStartIconClick = {
            requireActivity().finish()
        }
        // Resort Info
        binding.apply {
            tvResortName.text = postingDetail.resortName + " | " + postingDetail.roomCode
            tvLocation.text = postingDetail.location.displayName ?: postingDetail.location.name
            if (postingDetail.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Checkin Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                postingDetail.checkinDate,
                requireContext()
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkinDate, requireContext())


            tvCheckoutDate.text = Constant.getFormattedDate(
                postingDetail.checkoutDate,
                requireContext()
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkoutDate, requireContext())
        }

        // Set Unit Type Of Posting
        binding.apply {
            tvRoomName.text =
                "Chi Tiết Phòng | ${postingDetail.unitType.title} | ${postingDetail.roomCode}"

            // Bath
            tvNumBath.text = postingDetail.unitType.bathrooms.toString()
            tvBed.text = postingDetail.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to postingDetail.unitType.bedsFull,
                "bedsKing" to postingDetail.unitType.bedsKing,
                "bedsSofa" to postingDetail.unitType.bedsSofa,
                "bedsMurphy" to postingDetail.unitType.bedsMurphy,
                "bedsQueen" to postingDetail.unitType.bedsQueen,
                "bedsTwin" to postingDetail.unitType.bedsTwin
            )
            tvNumBed.text = postingDetail.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = postingDetail.unitType.kitchen
            tvNumKitchen.text = 1.toString()

            // Max Guest
            tvNumPerson.text = postingDetail.unitType.sleeps.toString()
            tvPerson.text = "${postingDetail.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later

        }

        // Cancel Policy
        binding.apply {
            if (postingDetail.cancelType.toString() == "null") {
                tvCancelPolicyDtb.text = "Không có"
            } else {
                val refundPolicy = RefundPolicy.getShortDescriptionFromName(
                    requireContext(),
                    postingDetail.cancelType.toString()
                )
                tvCancelPolicyDtb.text = refundPolicy
            }
        }

        // UI DTB
        binding.apply {
            tvCheckInDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkinDate, requireContext())
            tvCheckOutDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkoutDate, requireContext())
            tvNightDtb.text = "${postingDetail.nights} đêm"
            tvRoomPricePerNight.text =
                "${Constant.formatPriceLong(postingDetail.pricePerNights)} VNĐ / 1 đêm"
            tvEstimatedTotalPrice.text =
                "${Constant.formatPriceLong(postingDetail.totalPrice)} VNĐ / ${postingDetail.nights} đêm"
            tvPostedBy.text = "Đăng bởi ${postingDetail.ownerName}"

            Glide.with(requireContext())
                .load(postingDetail.unitType.photos)
                .placeholder(R.drawable.ic_image_tmp_holder)
                .error(R.drawable.ic_image_tmp_holder)
                .into(binding.imImageTimeshare)

        }

        // Data for Request
        binding.apply {
            tvPrice.text =
                "${Constant.formatPriceLong(postingDetail.totalPrice)} VNĐ / ${postingDetail.nights} đêm"

        }


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

    private fun requestButtonClick() {
        binding.ctrRequestButton.setOnClickListener {
            viewModel.setCurrentViewPager(1)
        }

    }


}