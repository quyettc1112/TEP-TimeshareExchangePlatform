package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemMyTimeshareBinding

class MyTimeshareAdapter: BaseAdapter<MyTimeshareModel, MyTimeshareAdapter.MyTimeshareViewHolder>() {

    inner class MyTimeshareViewHolder(binding: ItemMyTimeshareBinding) :
        BaseItemViewHolderCF<MyTimeshareModel, ItemMyTimeshareBinding>(binding) {
        override fun bind(item: MyTimeshareModel) {
            binding.tvResortName.text = item.name
            binding.tvRoomType.text = item.roomName
            binding.tvCheckinDate.text = "${item.checkInDate} - ${item.checkOutDate}"
            binding.tvNumberOfNight.text =  " | ${item.numberOfNight.toString()} đêm"
            binding.tvPrice.text = item.price.toString()
            Glide.with(binding.root.context).load(item.image).into(binding.imResortImage)
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyTimeshareModel> {
        return object : DiffUtil.ItemCallback<MyTimeshareModel>() {
            override fun areItemsTheSame(oldItem: MyTimeshareModel, newItem: MyTimeshareModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyTimeshareModel, newItem: MyTimeshareModel): Boolean {
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