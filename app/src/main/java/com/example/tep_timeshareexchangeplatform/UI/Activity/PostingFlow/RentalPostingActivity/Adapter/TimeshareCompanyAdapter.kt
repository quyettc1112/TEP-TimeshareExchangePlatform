package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.databinding.ItemTimeshareCompanyBinding

class TimeshareCompanyAdapter : BaseAdapter<String, TimeshareCompanyAdapter.TimeshareCompanyViewHolder>() {

    inner class TimeshareCompanyViewHolder(binding: ItemTimeshareCompanyBinding) : BaseItemViewHolderCF<String, ItemTimeshareCompanyBinding>(binding) {
        override fun bind(item: String) {
           Glide.with(binding.root).load(item).into(binding.ivTimeshareCompany)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeshareCompanyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeshareCompanyBinding.inflate(layoutInflater, parent, false)
        return TimeshareCompanyViewHolder(binding)
    }
}