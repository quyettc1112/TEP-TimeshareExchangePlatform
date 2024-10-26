package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyTimeshareBinding

class MyTimeshareAdapter: BaseAdapter<MyTimeshareResponse, MyTimeshareAdapter.MyTimeshareViewHolder>() {

    var onItemClick: ((MyTimeshareResponse) -> Unit)? = null

    inner class MyTimeshareViewHolder(binding: ItemMyTimeshareBinding) :
        BaseItemViewHolderCF<MyTimeshareResponse, ItemMyTimeshareBinding>(binding) {
        override fun bind(item: MyTimeshareResponse) {
            // Hide Unessary View
            binding.tvPrice.visibility = ViewGroup.GONE
            binding.tvNumberOfNight.visibility = ViewGroup.GONE
            binding.tvPrice.visibility = ViewGroup.GONE

            binding.tvResortName.text = item.resortName
            binding.tvRoomType.text = item.roomName
            binding.tvCheckinDate.text = "${item.startDate} - ${item.endDate}"
           /* Glide.with(binding.root.context).load(item.image).into(binding.imResortImage)*/

            binding.btnSelect.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyTimeshareResponse> {
        return object : DiffUtil.ItemCallback<MyTimeshareResponse>() {
            override fun areItemsTheSame(
                oldItem: MyTimeshareResponse,
                newItem: MyTimeshareResponse
            ): Boolean {
                return oldItem.timeShareId == newItem.timeShareId
            }

            override fun areContentsTheSame(
                oldItem: MyTimeshareResponse,
                newItem: MyTimeshareResponse
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