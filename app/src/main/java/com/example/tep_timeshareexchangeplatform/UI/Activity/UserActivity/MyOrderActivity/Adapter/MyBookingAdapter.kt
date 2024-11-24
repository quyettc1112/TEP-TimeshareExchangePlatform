package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyBookingStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyBookingBinding

class MyBookingAdapter : BaseAdapter<MyBookingResponse.Content, MyBookingAdapter.MyOrderViewHolder>() {

    var onItemClick: ((MyBookingResponse.Content) -> Unit)? = null
    var onFeedbackClick: ((MyBookingResponse.Content) -> Unit)? = null

    inner class MyOrderViewHolder(binding: ItemMyBookingBinding) :
        BaseItemViewHolderCF<MyBookingResponse.Content, ItemMyBookingBinding>(binding) {
        override fun bind(item: MyBookingResponse.Content) {


            binding.tvTimeshareName.text = "${item.resortName} | ${item.unitTypeTitle}"
            binding.tvCheckinDate.text =
                item.checkinDate?.let { Constant.getFormattedDate(it, binding.root.context) }
            binding.tvCheckinDayOfWeek.text =
                item.checkinDate?.let { Constant.getDayOfWeek(it, binding.root.context) }
            binding.tvCheckoutDate.text =
                item.checkoutDate?.let { Constant.getFormattedDate(it, binding.root.context) }
            binding.tvCheckoutDayOfWeek.text =
                item.checkoutDate?.let { Constant.getDayOfWeek(it, binding.root.context) }
            binding.tvTimeshareType.text = item.unitTypeTitle
            Glide.with(binding.root.context).load(item.logo).into(binding.imImageTimeshare)

            binding.tvBookingTupe.text = MyBookingStatus.fromApiStatus(item.source)?.getDescription(binding.root.context)


            if (item.source == "rental") {
                Glide.with(binding.root.context).load(R.drawable.ic_rental_booking).into(binding.imBookingType)
            } else {
                Glide.with(binding.root.context).load(R.drawable.ic_exchange_booking).into(binding.imBookingType)
            }


            /*${binding.root.context.getString(R.string.guests)}*/
            binding.tvGuestInfo.text = "Khách: ${item.primaryGuestName}"

            when (MyBookingStatus.fromApiStatus(item.status)) {
                MyBookingStatus.BOOKED -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.primaryColor,
                        R.color.white
                    )
                    binding.llFeedbackContainer.visibility = View.GONE
                }
                MyBookingStatus.CHECK_IN -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.blue_btn_search,
                        R.color.white
                    )
                    binding.llFeedbackContainer.visibility = View.GONE
                }
                MyBookingStatus.CHECKOUT -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.green_verify,
                        R.color.white
                    )
                    binding.llFeedbackContainer.visibility = View.VISIBLE
                }
                MyBookingStatus.NO_SHOW -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.status_unknown_bg,
                        R.color.status_unknown_text
                    )
                    binding.llFeedbackContainer.visibility = View.GONE
                }
                MyBookingStatus.CANCELED -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.status_rejected_text,
                        R.color.white
                    )
                    binding.llFeedbackContainer.visibility = View.GONE
                }
                MyBookingStatus.REFUND -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.status_rejected_text,
                        R.color.white
                    )
                    binding.llFeedbackContainer.visibility = View.GONE
                }
                MyBookingStatus.PAYMENT_COMPLETED -> {
                    applyStatusStyle(
                        binding.root.context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                    binding.llFeedbackContainer.visibility = View.GONE
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
            binding.llFeedbackContainer.setOnClickListener {
                onFeedbackClick?.invoke(item)
            }
            binding.llFeedbackContainer.visibility = View.GONE
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

    fun hideFeedbackById(bookingId: Int) {
        val currentList = differ.currentList.toMutableList()
        val index = currentList.indexOfFirst { it.bookingId == bookingId }
        if (index != -1) {
            val item = currentList[index].copy(isFeedbackGiven = true) // Cập nhật trạng thái
            currentList[index] = item
            submitList(currentList) // Cập nhật danh sách thông qua DiffUtil
        }
    }

    fun updateItemStatus(bookingId: Int, newStatus: String) {
        val currentList = differ.currentList.toMutableList()

        // Tìm vị trí của item có bookingId tương ứng
        val position = currentList.indexOfFirst { it.bookingId == bookingId }

        if (position != -1) { // Nếu tìm thấy item
            val oldItem = currentList[position]
            val updatedItem = oldItem.copy(status = newStatus) // Cập nhật trạng thái
            currentList[position] = updatedItem // Thay thế item tại vị trí

            Log.d("Adapter", "Item trước: $oldItem")
            Log.d("Adapter", "Item sau: $updatedItem")

            differ.submitList(currentList.toList()) // Cập nhật danh sách trong Adapter
        } else {
            Log.e("Adapter", "Không tìm thấy item với bookingId: $bookingId")
        }
    }
}