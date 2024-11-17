package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingsResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyPostingBinding

class MyExchangePostingAdapter(var context: MyExchangePostingActivity) :
    BaseAdapter<MyExchangePostingsResponse.Content, MyExchangePostingAdapter.MyExchangePostingViewHolder>() {

    var onItemClick: ((MyExchangePostingsResponse.Content) -> Unit)? = null


    inner class MyExchangePostingViewHolder(binding: ItemMyPostingBinding) :
        BaseItemViewHolderCF<MyExchangePostingsResponse.Content, ItemMyPostingBinding>(binding) {
        override fun bind(item: MyExchangePostingsResponse.Content) {
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
                        R.color.status_rejected_text
                    )
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
                // Photo
                Glide.with(binding.root.context)
                    .load(item.unitTypeDTO.photos)
                    .error(R.drawable.im_material_mn)
                    .placeholder(R.drawable.ripple_effect_white)
                    .into(binding.imResortImage)
            }

            // Package Info
            binding.apply {
                if (item.exchangePackageId != null) {
                    val exchangePackageEnum =
                        ExchangePackageEnum.getPackageById(item.exchangePackageId)
                    if (exchangePackageEnum != null) {
                        tvPackageName.text = exchangePackageEnum?.name
                        if (item.expired != null) {
                            tvExpiredDay.text = Constant.formatDateByLocale(
                                item.expired.toString() ?: "2024-12-31",
                                binding.root.context
                            )
                        }
                    }
                }


            }

            // Hide Price
            binding.llRoomPricePerNight.visibility = View.GONE

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }

        private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
            binding.apply {
                llStatusContainer.backgroundTintList = context.getColorStateList(backgroundColorRes)
                tvStatus.setTextColor(context.getColor(textColorRes))
                cardStatus.setStrokeColor(context.getColorStateList(textColorRes))
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyExchangePostingsResponse.Content> {
        return object : DiffUtil.ItemCallback<MyExchangePostingsResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: MyExchangePostingsResponse.Content,
                newItem: MyExchangePostingsResponse.Content
            ): Boolean {
                return oldItem.exchangePostingId == newItem.exchangePostingId
            }

            override fun areContentsTheSame(
                oldItem: MyExchangePostingsResponse.Content,
                newItem: MyExchangePostingsResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExchangePostingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMyPostingBinding.inflate(layoutInflater, parent, false)
        return MyExchangePostingViewHolder(binding)
    }

}