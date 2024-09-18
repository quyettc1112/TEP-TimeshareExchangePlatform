package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemLocationBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortBinding

class LocationAdapter: BaseAdapter<String, LocationAdapter.LocationViewHolder>() {

    var onitemCLickListener: ((String) -> Unit)? = null
    inner class LocationViewHolder(binding: ItemLocationBinding) : BaseItemViewHolderCF<String,ItemLocationBinding>(binding) {

        override fun bind(item: String) {
            binding.locationName.text = item
            binding.root.setOnClickListener {
                onitemCLickListener?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<String> {
        return object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }
}