package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyTimeshareBinding

class MyTimeshareAdapter: BaseAdapter<MyTimeshareResponse.Content, MyTimeshareAdapter.MyTimeshareViewHolder>() {

    var onItemClick: ((MyTimeshareResponse.Content) -> Unit)? = null

    inner class MyTimeshareViewHolder(binding: ItemMyTimeshareBinding) :
        BaseItemViewHolderCF<MyTimeshareResponse.Content, ItemMyTimeshareBinding>(binding) {
        override fun bind(item: MyTimeshareResponse.Content) {
            // Hide Unessary View

            binding.tvResortName.text = item.resortName
            binding.tvRoomType.text = "Tên Phòng: " + item.roomName
            binding.tvCheckinDate.text = Constant.formatDateByLocale(item.startDate, binding.root.context)
            binding.tvCheckOutDate.text =Constant.formatDateByLocale(item.endDate, binding.root.context)
           /* Glide.with(binding.root.context).load(item.image).into(binding.imResortImage)*/

            binding.btnSelect.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyTimeshareResponse.Content> {
        return object : DiffUtil.ItemCallback<MyTimeshareResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: MyTimeshareResponse.Content,
                newItem: MyTimeshareResponse.Content
            ): Boolean {
                return oldItem.timeShareId == newItem.timeShareId
            }

            override fun areContentsTheSame(
                oldItem: MyTimeshareResponse.Content,
                newItem: MyTimeshareResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTimeshareViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMyTimeshareBinding.inflate(layoutInflater, parent, false)
        return MyTimeshareViewHolder(binding)
    }

}