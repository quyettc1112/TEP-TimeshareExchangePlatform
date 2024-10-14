package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyOrderModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyOrderBinding

class MyOrderAdapter: BaseAdapter<MyOrderModel, MyOrderAdapter.MyOrderViewHolder>() {

    inner class MyOrderViewHolder(binding: ItemMyOrderBinding): BaseItemViewHolderCF<MyOrderModel, ItemMyOrderBinding>(binding) {
        override fun bind(item: MyOrderModel) {
            binding.tvOrderCode.text ="Ma đặt phòng: ${item.orderId}"
            binding.tvTimeshareName.text = item.timeshareName
            binding.tvCheckinDate.text = item.checkInDate
            binding.tvCheckinDay.text = item.checkInDay
            binding.tvCheckoutDate.text = item.checkOutDate
            binding.tvCheckoutDay.text = item.checkOutDay
            binding.tvTimeshareType.text = item.timeshareType
            binding.tvPrice.text = item.price
          /*  binding.tvCheckinDate.text = item.dateOfOrder
            binding.tvCheckoutDate.text = item.timeOfOrder*/
            binding.imTyepPayment.setImageResource(item.paymentTypeIcon)
            binding.imImageTimeshare.setImageResource(item.timeshareImage)

            when (item.status) {
                "Đã xác nhận" -> {
                    binding.tvStatus.text = item.status
                    binding.llStatusContainer.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.blue_btn_search) // Set background tint to blue
                    binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white)) // Set text color to white or any color you prefer
                }
                "Đang chờ xử lý" -> {
                    binding.tvStatus.text = item.status
                    binding.llStatusContainer.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.yellow_stroke_image) // Set background tint to yellow
                    binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black)) // Set text color to black or any color you prefer
                }
                "Đã hủy" -> {
                    binding.tvStatus.text = item.status
                    binding.llStatusContainer.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.red_light) // Set background tint to red
                    binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white)) // Set text color to white or any color you prefer
                }
                "Đang chờ xác nhận" -> {
                    binding.tvStatus.text = item.status
                    binding.llStatusContainer.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.green_ok) // Set background tint to green
                    binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black)) // Set text color to white or any color you prefer
                }
                else -> {
                    binding.tvStatus.text = item.status
                    binding.llStatusContainer.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.primary_background_F9) // Default background tint
                    binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black)) // Default text color
                }
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyOrderModel> {
        return object: DiffUtil.ItemCallback<MyOrderModel>() {
            override fun areItemsTheSame(oldItem: MyOrderModel, newItem: MyOrderModel): Boolean {
                return oldItem.orderId == newItem.orderId
            }

            override fun areContentsTheSame(oldItem: MyOrderModel, newItem: MyOrderModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMyOrderBinding.inflate(layoutInflater, parent, false)
        return MyOrderViewHolder(binding)
    }
}