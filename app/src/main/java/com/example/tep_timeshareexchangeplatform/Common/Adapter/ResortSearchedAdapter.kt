package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemLocationSearchedBinding

class ResortSearchedAdapter : BaseAdapter<ResortModelResponse.Content, ResortSearchedAdapter.ResortSearchedViewHolder>(){

    // Store the original unfiltered list
    inner class ResortSearchedViewHolder(binding: ItemLocationSearchedBinding) :
        BaseItemViewHolderCF<ResortModelResponse.Content, ItemLocationSearchedBinding>(binding) {
        override fun bind(item: ResortModelResponse.Content) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.logo)
                    .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.ripple_effect_white))
                    .into(binding.imLogo)
                tvResortName.text = item.resortName
                tvLocation.text = item.resortLocationDisplayName
            }


        }
    }

    // DiffUtil callback for differ
    override fun differCallBack(): DiffUtil.ItemCallback<ResortModelResponse.Content> {
        return object : DiffUtil.ItemCallback<ResortModelResponse.Content>() {
            override fun areItemsTheSame(oldItem: ResortModelResponse.Content, newItem: ResortModelResponse.Content): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResortModelResponse.Content, newItem: ResortModelResponse.Content): Boolean {
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