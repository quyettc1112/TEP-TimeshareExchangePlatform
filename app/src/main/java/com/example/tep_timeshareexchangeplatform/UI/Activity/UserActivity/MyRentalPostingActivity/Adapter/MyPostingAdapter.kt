package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingsResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingList.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyPostingBinding
import java.text.DecimalFormat

class MyPostingAdapter(var context: MyPostingActivity) :
    BaseAdapter<MyRentalPostingsResponse.Content, MyPostingAdapter.MyPostingViewHolder>() {


    var onItemClick: ((MyRentalPostingsResponse.Content) -> Unit)? = null
    var onItemPricingClick: ((MyRentalPostingsResponse.Content) -> Unit)? = null

    inner class MyPostingViewHolder(binding: ItemMyPostingBinding) :
        BaseItemViewHolderCF<MyRentalPostingsResponse.Content, ItemMyPostingBinding>(binding) {
        override fun bind(item: MyRentalPostingsResponse.Content) {
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
                Glide.with(binding.root.context)
                    .load(item.unitTypeDTO.photos)
                    .placeholder(R.drawable.ripple_effect_white)
                    .error(R.drawable.im_material_mn)
                    .into(binding.imResortImage)
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
                    val rentalPackageEnum = RentalPackageEnum.getPackageByName(item.rentalPackageName)
                    if (rentalPackageEnum != null) {
                        tvPackageName.text = rentalPackageEnum?.name
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

    override fun differCallBack(): DiffUtil.ItemCallback<MyRentalPostingsResponse.Content> {
        return object : DiffUtil.ItemCallback<MyRentalPostingsResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: MyRentalPostingsResponse.Content,
                newItem: MyRentalPostingsResponse.Content
            ): Boolean {
                return oldItem.rentalPostingId == newItem.rentalPostingId
            }

            override fun areContentsTheSame(
                oldItem: MyRentalPostingsResponse.Content,
                newItem: MyRentalPostingsResponse.Content
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