package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyPostingModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyPostingBinding

class MyPostingAdapter : BaseAdapter<MyPostingModel, MyPostingAdapter.MyPostingViewHolder>() {

    var onItemClick: ((MyPostingModel) -> Unit)? = null
    var onItemPricingClick: ((MyPostingModel) -> Unit)? = null
    inner class MyPostingViewHolder(binding : ItemMyPostingBinding)
        : BaseItemViewHolderCF<MyPostingModel, ItemMyPostingBinding> (binding) {
        override fun bind(item: MyPostingModel) {
            // check Verify
            if (item.isVerify) binding.llVerify.visibility = View.VISIBLE
            else binding.llVerify.visibility = View.GONE

            // Show Status
           /* if (item. == "Đã xác nhận") binding.llStatus.visibility = View.VISIBLE
            else binding.llStatus.visibility = View.GONE*/

            // Posting Info
            binding.tvResortName.text = "${item.name} | ${item.roomName}"
            binding.tvLocation.text = item.location
            binding.tvDate.text = item.stayDates

            // Price Info
            binding.tvPrice.text = item.priceRange

            // Package Info
            binding.tvPackageName.text = item.packageName
            binding.tvDuration.text = item.packageDuration

            // Price Demand
            binding.btnAcceptPrice.visibility = if (item.isPriceDemand) View.VISIBLE else View.GONE


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

    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyPostingModel> {
        return object : DiffUtil.ItemCallback<MyPostingModel>() {
            override fun areItemsTheSame(oldItem: MyPostingModel, newItem: MyPostingModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyPostingModel, newItem: MyPostingModel): Boolean {
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