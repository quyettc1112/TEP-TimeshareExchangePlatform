package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyOrderModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyBookingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyBookingBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyOrderBinding

class MyOrderAdapter : BaseAdapter<MyBookingResponse.Content, MyOrderAdapter.MyOrderViewHolder>() {

    var onItemClick: ((MyBookingResponse.Content) -> Unit)? = null

    inner class MyOrderViewHolder(binding: ItemMyBookingBinding) :
        BaseItemViewHolderCF<MyBookingResponse.Content, ItemMyBookingBinding>(binding) {
        override fun bind(item: MyBookingResponse.Content) {
            binding.tvTimeshareName.text = "${item.resortName} | ${item.unitTypeTitle}"
            binding.tvCheckinDate.text =
                Constant.getFormattedDate(item.checkinDate, binding.root.context)
            binding.tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(item.checkinDate, binding.root.context)
            binding.tvCheckoutDate.text =
                Constant.getFormattedDate(item.checkoutDate, binding.root.context)
            binding.tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(item.checkoutDate, binding.root.context)
            binding.tvTimeshareType.text = item.unitTypeTitle
            Glide.with(binding.root.context).load(item.logo).into(binding.imImageTimeshare)

            binding.tvBookingTupe.text = MyBookingStatus.fromApiStatus(item.source)?.getDescription(binding.root.context)

            /*${binding.root.context.getString(R.string.guests)}*/
            binding.tvGuestInfo.text = "Khách: ${item.primaryGuestName}"

            when (MyBookingStatus.fromApiStatus(item.status)) {
                MyBookingStatus.BOOKED -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.primaryColor,
                        R.color.white
                    )
                }
                MyBookingStatus.CHECK_IN -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.blue_btn_search,
                        R.color.white
                    )
                }
                MyBookingStatus.CHECKOUT -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.green_verify,
                        R.color.white
                    )
                }
                MyBookingStatus.NO_SHOW -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.status_unknown_bg,
                        R.color.status_unknown_text
                    )
                }
                MyBookingStatus.CANCELED -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.white,
                        R.color.status_rejected_text
                    )
                }
                MyBookingStatus.REFUND -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.white,
                        R.color.status_rejected_text
                    )
                }
                MyBookingStatus.PAYMENT_COMPLETED -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                }
                else -> {
                    // Default or unknown status case
                    applyStatusStyle(
                        binding.root.context,
                        R.color.status_unknown_bg,
                        R.color.status_unknown_text
                    )
                }
            }
            binding.tvStatus.text = MyBookingStatus.fromApiStatus(item.status)?.getDescription(binding.root.context)

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }

        private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
            binding.apply {
                // Nền
                llStatusContainer.backgroundTintList = context.getColorStateList(backgroundColorRes)

                // TExt
                tvStatus.setTextColor(context.getColor(textColorRes))

                // Stroke
                cardStatus.setStrokeColor(context.getColorStateList(R.color.white))

                // Background
                cardStatus.backgroundTintList = (context.getColorStateList(backgroundColorRes))
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyBookingResponse.Content> {
        return object : DiffUtil.ItemCallback<MyBookingResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: MyBookingResponse.Content,
                newItem: MyBookingResponse.Content
            ): Boolean {
                return oldItem.bookingId == newItem.bookingId
            }

            override fun areContentsTheSame(
                oldItem: MyBookingResponse.Content,
                newItem: MyBookingResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMyBookingBinding.inflate(layoutInflater, parent, false)
        return MyOrderViewHolder(binding)
    }
}