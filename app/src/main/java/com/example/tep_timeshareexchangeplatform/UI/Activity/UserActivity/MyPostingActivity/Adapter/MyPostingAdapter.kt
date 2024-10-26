package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyPostingModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyPostingBinding
import java.text.DecimalFormat

class MyPostingAdapter : BaseAdapter<PostingsResponse.Content, MyPostingAdapter.MyPostingViewHolder>() {

    var onItemClick: ((PostingsResponse.Content) -> Unit)? = null
    var onItemPricingClick: ((PostingsResponse.Content) -> Unit)? = null
    inner class MyPostingViewHolder(binding : ItemMyPostingBinding)
        : BaseItemViewHolderCF<PostingsResponse.Content, ItemMyPostingBinding> (binding) {
        override fun bind(item: PostingsResponse.Content) {
            // check Verify
            if (item.isVerify) binding.llVerify.visibility = View.VISIBLE
            else binding.llVerify.visibility = View.GONE

            // Show Status
           /* if (item. == "Đã xác nhận") binding.llStatus.visibility = View.VISIBLE
            else binding.llStatus.visibility = View.GONE*/

            // Posting Info
            binding.tvResortName.text = "${item.resortName} | ${item.roomName}"
            binding.tvLocation.text = item.address
            binding.tvDate.text = item.checkinDate + " - " + item.checkoutDate

            // Price Info
            binding.tvPrice.text = "${formatPrice(item.pricePerNights)} VND | 1 đêm"

            // Status
            binding.tvStatus.text = item.status
            // Package Info
           /* binding.tvPackageName.text = item.packageName
            binding.tvDuration.text = item.packageDuration*/

            // Price Demand
            /*binding.btnAcceptPrice.visibility = if (item.isPriceDemand) View.VISIBLE else View.GONE*/


            // Hide Unused Info
            binding.tvNumberOfNight.visibility = View.GONE


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

    }

    override fun differCallBack(): DiffUtil.ItemCallback<PostingsResponse.Content> {
        return object : DiffUtil.ItemCallback<PostingsResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: PostingsResponse.Content,
                newItem: PostingsResponse.Content
            ): Boolean {
                return oldItem.rentalPostingId == newItem.rentalPostingId
            }

            override fun areContentsTheSame(
                oldItem: PostingsResponse.Content,
                newItem: PostingsResponse.Content
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