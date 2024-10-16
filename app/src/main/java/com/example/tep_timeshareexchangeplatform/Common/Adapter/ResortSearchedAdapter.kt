package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.ResortModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemLocationSearchedBinding

class ResortSearchedAdapter : BaseAdapter<ResortModel.Content, ResortSearchedAdapter.ResortSearchedViewHolder>(){

    // Store the original unfiltered list
    inner class ResortSearchedViewHolder(binding: ItemLocationSearchedBinding) :
        BaseItemViewHolderCF<ResortModel.Content, ItemLocationSearchedBinding>(binding) {
        override fun bind(item: ResortModel.Content) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.logo)
                    .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.ripple_effect_white))
                    .into(binding.imLogo)
                tvResortName.text = item.resortName
                tvLocation.text = item.address
            }


        }
    }

    // DiffUtil callback for differ
    override fun differCallBack(): DiffUtil.ItemCallback<ResortModel.Content> {
        return object : DiffUtil.ItemCallback<ResortModel.Content>() {
            override fun areItemsTheSame(oldItem: ResortModel.Content, newItem: ResortModel.Content): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResortModel.Content, newItem: ResortModel.Content): Boolean {
                return oldItem == newItem
            }
        }
    }

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortSearchedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationSearchedBinding.inflate(layoutInflater, parent, false)
        return ResortSearchedViewHolder(binding)
    }


}