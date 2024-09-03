package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.TimeshareModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemTimeshareBinding

class SuggestTimeshareAdapter: BaseAdapter<TimeshareModel, SuggestTimeshareAdapter.SuggestTimeshareViewHolder>() {


    var onItemClick: ((TimeshareModel) -> Unit)? = null
    var onFavoriteClick: ((TimeshareModel) -> Unit)? = null

    inner class SuggestTimeshareViewHolder(binding: ItemTimeshareBinding): BaseItemViewHolderCF<TimeshareModel, ItemTimeshareBinding>(binding) {
        override fun bind(item: TimeshareModel) {
            Glide.with(binding.imImageTimeshare.context)
                .load(item.imageTimeshare)
                .into(binding.imImageTimeshare)
            binding.tvTimeShreName.text = item.timeshareName
            binding.tvLocation.text = item.location
            binding.tvDate.text = item.date
            binding.tvRoom.text = item.roomDetails
            binding.tvPrice.text = item.price
            binding.tvNumberOfNight.text = item.numberOfNights

            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }
            binding.imFavorite.setOnClickListener {
                onFavoriteClick?.let { it1 -> it1(item) }
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<TimeshareModel> {
        return object : DiffUtil.ItemCallback<TimeshareModel>() {
            override fun areItemsTheSame(oldItem: TimeshareModel, newItem: TimeshareModel): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: TimeshareModel, newItem: TimeshareModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestTimeshareViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeshareBinding.inflate(inflater, parent, false)
        return SuggestTimeshareViewHolder(binding)
    }

}