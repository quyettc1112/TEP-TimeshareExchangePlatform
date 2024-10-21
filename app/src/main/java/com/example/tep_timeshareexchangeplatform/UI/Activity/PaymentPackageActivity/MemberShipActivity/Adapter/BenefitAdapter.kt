package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.MemberShipActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.databinding.ItemPackageBenefitBinding
import java.text.DecimalFormat

class BenefitAdapter: BaseAdapter<String, BenefitAdapter.BenefitViewHolder>() {

    inner class BenefitViewHolder(binding: ItemPackageBenefitBinding): BaseItemViewHolderCF<String, ItemPackageBenefitBinding>(binding){
        override fun bind(item: String) {
            binding.tvBenefit.text = item
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<String> {
        return object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPackageBenefitBinding.inflate(layoutInflater, parent, false)
        return BenefitViewHolder(binding)
    }
}