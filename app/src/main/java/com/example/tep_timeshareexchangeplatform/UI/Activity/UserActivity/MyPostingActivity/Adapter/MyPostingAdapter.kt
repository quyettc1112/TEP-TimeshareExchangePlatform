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
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyPostingBinding
import java.text.DecimalFormat

class MyPostingAdapter(var context: MyPostingActivity) :
    BaseAdapter<MyPostingResponse.Content, MyPostingAdapter.MyPostingViewHolder>() {


    var onItemClick: ((MyPostingResponse.Content) -> Unit)? = null
    var onItemPricingClick: ((MyPostingResponse.Content) -> Unit)? = null

    inner class MyPostingViewHolder(binding: ItemMyPostingBinding) :
        BaseItemViewHolderCF<MyPostingResponse.Content, ItemMyPostingBinding>(binding) {
        override fun bind(item: MyPostingResponse.Content) {
            // check Verify
            if (item.isVerify) binding.llVerify.visibility = View.VISIBLE
            else binding.llVerify.visibility = View.GONE

            // Show Status
            when (MyPostingStatus.fromApiStatus(item.status)) {
                MyPostingStatus.PENDING_APPROVAL -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                    binding.btnAcceptPrice.visibility = View.GONE
                }

                MyPostingStatus.AWAITING_CONFIRMATION -> {
                    applyStatusStyle(
                        context,
                        R.color.status_awaiting_confirmation_bg,
                        R.color.status_awaiting_confirmation_text
                    )
                    binding.btnAcceptPrice.visibility = View.VISIBLE
                }

                MyPostingStatus.PROCESSING -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.green_verify
                    )
                    binding.btnAcceptPrice.visibility = View.GONE
                }

                MyPostingStatus.COMPLETED -> {
                    applyStatusStyle(
                        context,
                        R.color.blue_header_section,
                        R.color.blue_full
                    )
                    binding.btnAcceptPrice.visibility = View.GONE
                }

                MyPostingStatus.REJECTED -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_rejected_text
                    )
                    binding.btnAcceptPrice.visibility = View.GONE
                }

                MyPostingStatus.PENDING_PRICING -> {
                    applyStatusStyle(
                        context,
                        R.color.status_awaiting_confirmation_bg,
                        R.color.status_awaiting_confirmation_text
                    )
                    binding.btnAcceptPrice.visibility = View.GONE
                }

                MyPostingStatus.CLOSED -> {
                    applyStatusStyle(
                        context,
                        R.color.status_closed_bg,
                        R.color.status_closed_text
                    )
                    binding.btnAcceptPrice.visibility = View.GONE
                }

                MyPostingStatus.REJECT_PRICE -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_rejected_text)
                    binding.btnAcceptPrice.visibility = View.GONE
                }

                else -> {
                    // Default or unknown status case
                    applyStatusStyle(
                        context,
                        R.color.status_unknown_bg,
                        R.color.status_unknown_text
                    )
                    binding.btnAcceptPrice.visibility = View.GONE
                }
            }
            binding.tvStatus.text = MyPostingStatus.fromApiStatus(item.status)?.getDescription(context)

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
            if (item.pricePerNights != null && item.pricePerNights != 0) {
                binding.tvRoomPricePerNight.text = "${formatPrice(item.pricePerNights)}đ /đêm"
            } else {
                binding.tvRoomPricePerNight.text = MyPostingStatus.fromApiStatus(item.status)?.getDescription(context)
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

    override fun differCallBack(): DiffUtil.ItemCallback<MyPostingResponse.Content> {
        return object : DiffUtil.ItemCallback<MyPostingResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: MyPostingResponse.Content,
                newItem: MyPostingResponse.Content
            ): Boolean {
                return oldItem.rentalPostingId == newItem.rentalPostingId
            }

            override fun areContentsTheSame(
                oldItem: MyPostingResponse.Content,
                newItem: MyPostingResponse.Content
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