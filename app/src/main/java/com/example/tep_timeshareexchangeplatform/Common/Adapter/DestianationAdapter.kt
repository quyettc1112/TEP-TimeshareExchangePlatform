package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.DestinationModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemTouristDestinationBinding

class DestianationAdapter: BaseAdapter<DestinationModel, DestianationAdapter.DestinationViewHolder>()  {

    inner class DestinationViewHolder(binding: ItemTouristDestinationBinding): BaseItemViewHolderCF<DestinationModel, ItemTouristDestinationBinding>(binding) {
        override fun bind(item: DestinationModel) {
            Glide.with(binding.imageView.context)
                .load(item.destinationImage)
                .into(binding.imageView)
           /* binding.t.text = item.name
            binding.tvDestinationLocation.text = item.location
            binding.tvDestinationPrice.text = item.price*/
        }


    }

    override fun differCallBack(): DiffUtil.ItemCallback<DestinationModel> {
        return object : DiffUtil.ItemCallback<DestinationModel>() {
            override fun areItemsTheSame(oldItem: DestinationModel, newItem: DestinationModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DestinationModel, newItem: DestinationModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTouristDestinationBinding.inflate(inflater, parent, false)
        return DestinationViewHolder(binding)
    }
}