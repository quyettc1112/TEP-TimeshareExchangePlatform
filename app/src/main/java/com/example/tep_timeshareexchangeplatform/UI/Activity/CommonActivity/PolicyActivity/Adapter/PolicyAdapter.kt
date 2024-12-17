package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PolicyActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Policy.PolicyResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemPolicyBinding

class PolicyAdapter : BaseAdapter<PolicyResponse.PolicyResponseItem, PolicyAdapter.PolicyViewHoder>(){
    inner class PolicyViewHoder(binding: ItemPolicyBinding) :
        BaseItemViewHolderCF<PolicyResponse.PolicyResponseItem, ItemPolicyBinding>(binding) {
        override fun bind(item: PolicyResponse.PolicyResponseItem) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<PolicyResponse.PolicyResponseItem> {
        return object : DiffUtil.ItemCallback<PolicyResponse.PolicyResponseItem>() {
            override fun areItemsTheSame(
                oldItem: PolicyResponse.PolicyResponseItem,
                newItem: PolicyResponse.PolicyResponseItem
            ): Boolean {
                return oldItem.policyId == newItem.policyId
            }

            override fun areContentsTheSame(
                oldItem: PolicyResponse.PolicyResponseItem,
                newItem: PolicyResponse.PolicyResponseItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyViewHoder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPolicyBinding.inflate(layoutInflater, parent, false)
        return PolicyViewHoder(binding)
    }
}