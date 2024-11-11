package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.Adapter.BenefitAdapter
import com.example.tep_timeshareexchangeplatform.databinding.ItemPackagePostingBinding
import java.text.DecimalFormat

class PackagePostingAdapter: BaseAdapter<PackageModel, PackagePostingAdapter.PackageViewHolder>() {

    inner class PackageViewHolder(binding: ItemPackagePostingBinding) : BaseItemViewHolderCF<PackageModel, ItemPackagePostingBinding> (binding){
        override fun bind(item: PackageModel) {
            var benefitAdapter = BenefitAdapter()
            benefitAdapter.submitList(item.listBenefit)
            binding.tvPackageName.text = item.name
            binding.tvPackagePrice.text = "${formatPrice(item.price)} VND"
            binding.tvPackageDescription.text = item.description
            binding.rvFeatures.let {
                it.adapter = benefitAdapter
                it.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(it.context)
            }

        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<PackageModel> {
        return object : DiffUtil.ItemCallback<PackageModel>() {
            override fun areItemsTheSame(oldItem: PackageModel, newItem: PackageModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PackageModel, newItem: PackageModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPackagePostingBinding.inflate(layoutInflater, parent, false)
        return PackageViewHolder(binding)
    }
}