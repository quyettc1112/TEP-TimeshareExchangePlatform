package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PostStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyPostingBinding
import java.text.DecimalFormat

class MyPostingAdapter(var context: MyPostingActivity) :
    BaseAdapter<MyPostingResponse.MyPostingResponseItem, MyPostingAdapter.MyPostingViewHolder>() {


    var onItemClick: ((MyPostingResponse.MyPostingResponseItem) -> Unit)? = null
    var onItemPricingClick: ((MyPostingResponse.MyPostingResponseItem) -> Unit)? = null

    inner class MyPostingViewHolder(binding: ItemMyPostingBinding) :
        BaseItemViewHolderCF<MyPostingResponse.MyPostingResponseItem, ItemMyPostingBinding>(binding) {
        override fun bind(item: MyPostingResponse.MyPostingResponseItem) {
            // check Verify
            if (item.isVerify) binding.llVerify.visibility = View.VISIBLE
            else binding.llVerify.visibility = View.GONE

            // Show Status
            when (PostStatus.fromApiStatus(item.status)) {
                PostStatus.PENDING_APPROVAL -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                }

                PostStatus.AWAITING_CONFIRMATION -> {
                    applyStatusStyle(
                        context,
                        R.color.status_awaiting_confirmation_bg,
                        R.color.status_awaiting_confirmation_text
                    )
                    binding.btnAcceptPrice.visibility = View.VISIBLE
                }

                PostStatus.PROCESSING -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.green_verify
                    )
                }

                PostStatus.COMPLETED -> {
                    applyStatusStyle(
                        context,
                        R.color.blue_header_section,
                        R.color.blue_full
                    )
                }

                PostStatus.REJECTED -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_rejected_text
                    )
                }

                PostStatus.PENDING_PRICING -> {
                    applyStatusStyle(
                        context,
                        R.color.status_awaiting_confirmation_bg,
                        R.color.status_awaiting_confirmation_text
                    )
                }

                PostStatus.CLOSED -> {
                    applyStatusStyle(
                        context,
                        R.color.status_closed_bg,
                        R.color.status_closed_text
                    )
                }

                else -> {
                    // Default or unknown status case
                    applyStatusStyle(
                        context,
                        R.color.status_unknown_bg,
                        R.color.status_unknown_text
                    )
                }
            }
            binding.tvStatus.text = PostStatus.fromApiStatus(item.status)?.getDescription(context)

            // Posting Info
            binding.apply {
                tvResortName.text = "${item.resortName}"
                tvRoomName.text =
                    "Loại Phòng: ${item.unitTypeDTO.title}, Tên Phòng: ${item.roomName}"
                tvLocation.text = item.address
                tvCheckInDate.text =
                    Constant.formatDateByLocale(item.checkinDate, binding.root.context)
                tvCheckOutDate.text =
                    Constant.formatDateByLocale(item.checkoutDate, binding.root.context)
            }

            // Price
            if (item.rentalPackageId == 1 || item.rentalPackageId == 2) {
                binding.tvRoomPricePerNight.text = "${formatPrice(item.pricePerNights)}đ /đêm"
            } else {
                binding.tvRoomPricePerNight.text = "Đang Chờ Định Giá"
            }

            // Package Info
            binding.apply {
                if (item.rentalPackageName != null) {
                    val packageEnum = PackageEnum.getPackageByName(item.rentalPackageName)
                    if (packageEnum != null) {
                        tvPackageName.text = packageEnum?.name
                        if (item.expiredDate != null) {
                            tvExpiredDay.text = Constant.formatDateByLocale(
                                item.expiredDate.toString() ?: "2024-12-31",
                                binding.root.context
                            )
                        }

                    }
                }


            }


            // Event Click
            binding.btnAcceptPrice.setOnClickListener {
                onItemPricingClick?.invoke(item)
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

        private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
            binding.apply {
                llStatusContainer.backgroundTintList = context.getColorStateList(backgroundColorRes)
                tvStatus.setTextColor(context.getColor(textColorRes))
                cardStatus.setStrokeColor(context.getColorStateList(textColorRes))
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyPostingResponse.MyPostingResponseItem> {
        return object : DiffUtil.ItemCallback<MyPostingResponse.MyPostingResponseItem>() {
            override fun areItemsTheSame(
                oldItem: MyPostingResponse.MyPostingResponseItem,
                newItem: MyPostingResponse.MyPostingResponseItem
            ): Boolean {
                return oldItem.rentalPostingId == newItem.rentalPostingId
            }

            override fun areContentsTheSame(
                oldItem: MyPostingResponse.MyPostingResponseItem,
                newItem: MyPostingResponse.MyPostingResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMyPostingBinding.inflate(layoutInflater, parent, false)
        return MyPostingViewHolder(binding)
    }
}