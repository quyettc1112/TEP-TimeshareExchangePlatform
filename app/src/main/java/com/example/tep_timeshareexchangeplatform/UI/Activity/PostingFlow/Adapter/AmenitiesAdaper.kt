package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemAmenitiesBinding

class AmenitiesAdaper: BaseAdapter<AmenitiesModel, AmenitiesAdaper.AmenitiesViewHolder>() {
    val onItemChecked: (AmenitiesModel) -> Unit = {}

    inner class AmenitiesViewHolder(binding: ItemAmenitiesBinding) : BaseItemViewHolderCF<AmenitiesModel, ItemAmenitiesBinding>(binding) {
        override fun bind(item: AmenitiesModel) {
            binding.checkBoxItem.text = item.name

            binding.checkBoxItem.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                onItemChecked(item)
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<AmenitiesModel> {
        return object : DiffUtil.ItemCallback<AmenitiesModel>() {
            override fun areItemsTheSame(oldItem: AmenitiesModel, newItem: AmenitiesModel): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: AmenitiesModel, newItem: AmenitiesModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenitiesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAmenitiesBinding.inflate(layoutInflater, parent, false)
        return AmenitiesViewHolder(binding)
    }
}